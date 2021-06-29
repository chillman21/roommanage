package club.chillman.roommanage.service.impl;

import club.chillman.roommanage.entity.ConferenceRoom;
import club.chillman.roommanage.entity.dto.RoomQuery;
import club.chillman.roommanage.mapper.ConferenceRoomMapper;
import club.chillman.roommanage.service.ConferenceRoomService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author NIU
 * @createTime 2021/4/11 2:28
 */
@Service
public class ConferenceRoomServiceImpl extends ServiceImpl<ConferenceRoomMapper, ConferenceRoom> implements ConferenceRoomService {
    @Override
    public void pageQuery(Page<ConferenceRoom> pageParam, RoomQuery roomQuery) {
        QueryWrapper<ConferenceRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_create");

        if (roomQuery == null){
            baseMapper.selectPage(pageParam, queryWrapper);
            return;
        }
        String fullName = roomQuery.getFullName();
        String building = roomQuery.getBuilding();
        String floor = roomQuery.getFloor();
        String position = roomQuery.getPosition();
        String roomNo = roomQuery.getRoomNo();
        /**
         * ge、gt、le、lt
         * 大于等于、大于、小于等于、小于
         * eq、ne、between
         * 等于、不等于、在区间内
         */
        if (!StringUtils.isEmpty(fullName)) {
            queryWrapper.like("full_name", fullName);
        }

        if (!StringUtils.isEmpty(building)) {
            queryWrapper.eq("building", building);
        }

        if (!StringUtils.isEmpty(floor)) {
            queryWrapper.eq("floor", floor);
        }

        if (!StringUtils.isEmpty(position)) {
            queryWrapper.eq("position", position);
        }

        if (!StringUtils.isEmpty(roomNo)) {
            queryWrapper.eq("room_no", roomNo);
        }
        baseMapper.selectPage(pageParam, queryWrapper);
    }

    @Override
    public void findFreeRoom(Page<ConferenceRoom> pageParam, RoomQuery roomQuery) {
        List<String> roomIdList = baseMapper.findFreeRoom();
        QueryWrapper<ConferenceRoom> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_create");
        queryWrapper.in("id", roomIdList);
        if (roomQuery == null){
            baseMapper.selectPage(pageParam, queryWrapper);
            return;
        }
        String fullName = roomQuery.getFullName();
        String building = roomQuery.getBuilding();
        String floor = roomQuery.getFloor();
        String position = roomQuery.getPosition();
        String roomNo = roomQuery.getRoomNo();
        if (!StringUtils.isEmpty(fullName)) {
            queryWrapper.like("full_name", fullName);
        }

        if (!StringUtils.isEmpty(building)) {
            queryWrapper.eq("building", building);
        }

        if (!StringUtils.isEmpty(floor)) {
            queryWrapper.eq("floor", floor);
        }

        if (!StringUtils.isEmpty(position)) {
            queryWrapper.eq("position", position);
        }

        if (!StringUtils.isEmpty(roomNo)) {
            queryWrapper.eq("room_no", roomNo);
        }
        baseMapper.selectPage(pageParam, queryWrapper);
//        if (StringUtils.isEmpty(building) && StringUtils.isEmpty(floor) && StringUtils.isEmpty(fullName)) {
//            return rooms;
//        }
//        rooms = rooms.stream()
//                .filter(o -> StringUtils.isEmpty(building) || o.getBuilding().equals(building))
//                .filter(o -> StringUtils.isEmpty(floor) || o.getFloor().equals(floor))
//                .filter(o -> StringUtils.isEmpty(fullName) || o.getFullName().contains(fullName))
//                .collect(Collectors.toList());
//        return rooms;
    }

    @Override
    public List<ConferenceRoom> getStarList(String userId) {
        return baseMapper.getStarList(userId);
    }


}
