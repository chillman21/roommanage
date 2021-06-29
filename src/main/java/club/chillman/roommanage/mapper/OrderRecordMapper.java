package club.chillman.roommanage.mapper;

import club.chillman.roommanage.entity.OrderRecord;
import club.chillman.roommanage.entity.vo.OrderRecordVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author NIU
 * @createTime 2021/4/11 15:22
 */
public interface OrderRecordMapper extends BaseMapper<OrderRecord> {
    List<OrderRecord> findRecordsIn7Days(String roomId);
    IPage<OrderRecordVO> findAllRecordsById(IPage<OrderRecordVO> page, String userId);
    IPage<OrderRecordVO> findCompletedRecordsById(IPage<OrderRecordVO> page, String userId);
    IPage<OrderRecordVO> findReservedRecordsById(IPage<OrderRecordVO> page, String userId);
}
