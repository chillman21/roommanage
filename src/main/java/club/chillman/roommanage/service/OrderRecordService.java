package club.chillman.roommanage.service;

import club.chillman.roommanage.entity.OrderRecord;
import club.chillman.roommanage.entity.dto.ReserveDTO;
import club.chillman.roommanage.entity.vo.OrderRecordVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author NIU
 * @createTime 2021/4/11 15:21
 */
public interface OrderRecordService extends IService<OrderRecord> {
    List<OrderRecord> findRecordsIn7Days(String roomId);

    boolean reserveSubmit(ReserveDTO reserveDTO, String userId);

    boolean reserveModify(ReserveDTO reserveDTO, String orderId);

    IPage<OrderRecordVO> findAllRecordsById(String userId, Long current, Long limit);

    IPage<OrderRecordVO> findCompletedRecordsById(String userId, Long current, Long limit);

    IPage<OrderRecordVO> findReservedRecordsById(String userId, Long current, Long limit);
}
