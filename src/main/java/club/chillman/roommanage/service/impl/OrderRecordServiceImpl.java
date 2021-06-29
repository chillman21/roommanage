package club.chillman.roommanage.service.impl;

import club.chillman.roommanage.entity.ConferenceRoom;
import club.chillman.roommanage.entity.OrderRecord;
import club.chillman.roommanage.entity.dto.ReserveDTO;
import club.chillman.roommanage.entity.vo.OrderRecordVO;
import club.chillman.roommanage.exception.RoomManageException;
import club.chillman.roommanage.mapper.OrderRecordMapper;
import club.chillman.roommanage.service.OrderRecordService;
import club.chillman.roommanage.utils.OrderNoUtil;
import club.chillman.roommanage.utils.SpringBootRedisLock;
import club.chillman.roommanage.utils.TimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author NIU
 * @createTime 2021/4/11 15:23
 */
@Service
public class OrderRecordServiceImpl extends ServiceImpl<OrderRecordMapper, OrderRecord> implements OrderRecordService {

    //分布式锁逻辑已封装，不在一个JVM上所以不需要volatile
    @Autowired
    private SpringBootRedisLock redisLock;

    @Override
    public List<OrderRecord> findRecordsIn7Days(String roomId) {
        List<OrderRecord> records = baseMapper.findRecordsIn7Days(roomId);
        return records;
    }

    @Override
    public boolean reserveSubmit(ReserveDTO reserveDTO, String userId) {
        int insert = 0;
        try {
            String dateSdf = TimeUtil.getDateSdf(reserveDTO.getStartTime());
            String key = reserveDTO.getRoomId() + "-" + dateSdf;
            //双重检测锁
            if (checkRecordConflictsExist(reserveDTO))
                throw new RoomManageException(404, "该会议室已被抢占");
            redisLock.lock(key, 5L, 600L);
            if (checkRecordConflictsExist(reserveDTO)) {
                redisLock.unlock(key);
                throw new RoomManageException(404, "该会议室已被抢占");
            }
            //线程持有锁：开始
            OrderRecord record = new OrderRecord();
            //获取订单号
            record.setId(OrderNoUtil.getOrderNo());
            record.setUserId(userId);
            record.setRoomId(reserveDTO.getRoomId());
            record.setStartTime(TimeUtil.str2Date(reserveDTO.getStartTime()));
            record.setEndTime(TimeUtil.str2Date(reserveDTO.getEndTime()));

            insert = baseMapper.insert(record);
            //线程释放锁：结束
            redisLock.unlock(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
            //throw new RoomManageException(404, e.getMessage());
        }
        return insert > 0;
    }

    @Override
    public boolean reserveModify(ReserveDTO reserveDTO, String orderId) {
        int update = 0;
        try {
            String dateSdf = TimeUtil.getDateSdf(reserveDTO.getStartTime());
            String key = reserveDTO.getRoomId() + "-" + dateSdf;
            //双重检测锁
            if (checkRecordConflictsExist(reserveDTO, orderId)) throw new RoomManageException(404, "该会议室已被抢占");
            redisLock.lock(key, 5L, 600L);
            if (checkRecordConflictsExist(reserveDTO, orderId)) throw new RoomManageException(404, "该会议室已被抢占");
            //线程持有锁：开始
            OrderRecord record = new OrderRecord();
            //获取订单号
            record.setId(orderId);
            record.setRoomId(reserveDTO.getRoomId());
            record.setStartTime(TimeUtil.str2Date(reserveDTO.getStartTime()));
            record.setEndTime(TimeUtil.str2Date(reserveDTO.getEndTime()));

            update = baseMapper.updateById(record);
            //线程释放锁：结束
            redisLock.unlock(key);
        } catch (Exception e) {
            return false;
        }
        return update > 0;
    }




    @Override
    public IPage<OrderRecordVO> findAllRecordsById(String userId, Long current, Long limit) {
        Page<OrderRecordVO> page = new Page<>(current,limit);
        return baseMapper.findAllRecordsById(page, userId);
    }

    @Override
    public IPage<OrderRecordVO> findCompletedRecordsById(String userId, Long current, Long limit) {
        Page<OrderRecordVO> page = new Page<>(current,limit);
        return baseMapper.findCompletedRecordsById(page, userId);
    }

    @Override
    public IPage<OrderRecordVO> findReservedRecordsById(String userId, Long current, Long limit) {
        Page<OrderRecordVO> page = new Page<>(current,limit);
        return baseMapper.findReservedRecordsById(page, userId);
    }


    /**
     *  检查会议室预订是否存在冲突
     * @param reserveDTO 预订对象，包含房间id和起止时间点
     * @return 冲突存在返回 true, 不存在返回 false
     */
    private boolean checkRecordConflictsExist(ReserveDTO reserveDTO) {
        //获取开始时间和结束时间
        String roomId = reserveDTO.getRoomId();
        String startTime = reserveDTO.getStartTime();
        Date startDate = TimeUtil.str2Date(startTime);
        String endTime = reserveDTO.getEndTime();
        Date endDate = TimeUtil.str2Date(endTime);
        //结束时间必须大于等于开始时间
        if (endDate.before(startDate)) throw new RoomManageException(404, "结束时间大于开始时间，预定无效");
        QueryWrapper<OrderRecord> queryWrapper = new QueryWrapper<>();
        //房间号匹配
        queryWrapper.eq("room_id", roomId);
        queryWrapper.eq("is_deleted", "0");
        //日期前缀匹配
        queryWrapper.likeRight("start_time",TimeUtil.getDate(startTime));
        List<OrderRecord> list = baseMapper.selectList(queryWrapper);
        //若当日该房间没有预订，则冲突一定不存在
        if (list.isEmpty()) return false;
        //遍历当天的预订记录，使用冲突检测算法筛选
        for (OrderRecord record : list) {
            Date existedStart = record.getStartTime();//表中已存在的起始时间
            Date existedEnd = record.getEndTime();//表中已存在的结束时间
            if ((existedStart.before(startDate) || existedStart.equals(startDate)) && startDate.before(existedEnd)) return true;
            if ((startDate.before(existedStart) || startDate.equals(existedStart)) && existedStart.before(endDate)) return true;
        }
        return false;
    }

    private boolean checkRecordConflictsExist(ReserveDTO reserveDTO, String orderId) {
        //获取开始时间和结束时间
        String roomId = reserveDTO.getRoomId();
        String startTime = reserveDTO.getStartTime();
        Date startDate = TimeUtil.str2Date(startTime);
        String endTime = reserveDTO.getEndTime();
        Date endDate = TimeUtil.str2Date(endTime);
        //结束时间必须大于等于开始时间
        if (endDate.before(startDate)) throw new RoomManageException(404, "结束时间大于开始时间，预定无效");
        QueryWrapper<OrderRecord> queryWrapper = new QueryWrapper<>();
        //房间号匹配
        queryWrapper.ne("id", orderId);
        queryWrapper.eq("room_id", roomId);
        queryWrapper.eq("is_deleted", "0");
        //日期前缀匹配
        queryWrapper.likeRight("start_time",TimeUtil.getDate(startTime));
        List<OrderRecord> list = baseMapper.selectList(queryWrapper);
        //若当日没有预订，则冲突一定不存在
        if (list.isEmpty()) return false;
        //遍历当天的预订记录，使用冲突检测算法筛选
        for (OrderRecord record : list) {
            Date existedStart = record.getStartTime();
            Date existedEnd = record.getEndTime();
            if ((existedStart.before(startDate) || existedStart.equals(startDate)) && startDate.before(existedEnd)) return true;
            if ((startDate.before(existedStart) || startDate.equals(existedStart)) && existedStart.before(endDate)) return true;
        }
        return false;
    }
}
