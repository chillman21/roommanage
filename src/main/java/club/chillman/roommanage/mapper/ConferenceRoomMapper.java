package club.chillman.roommanage.mapper;

import club.chillman.roommanage.entity.ConferenceRoom;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author NIU
 * @createTime 2021/4/11 2:29
 */
public interface ConferenceRoomMapper extends BaseMapper<ConferenceRoom> {
    List<String> findFreeRoom();
    List<ConferenceRoom> getStarList(String userId);
}
