package club.chillman.roommanage.service;

import club.chillman.roommanage.entity.ConferenceRoom;
import club.chillman.roommanage.entity.dto.RoomQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author NIU
 * @createTime 2021/4/11 2:25
 */
public interface ConferenceRoomService extends IService<ConferenceRoom> {
    void pageQuery(Page<ConferenceRoom> pageParam, RoomQuery roomQuery);
    void findFreeRoom(Page<ConferenceRoom> pageParam, RoomQuery roomQuery);
    List<ConferenceRoom> getStarList(String userId);
}
