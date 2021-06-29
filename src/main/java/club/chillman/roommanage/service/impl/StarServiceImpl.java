package club.chillman.roommanage.service.impl;

import club.chillman.roommanage.entity.OrderRecord;
import club.chillman.roommanage.entity.Star;
import club.chillman.roommanage.mapper.OrderRecordMapper;
import club.chillman.roommanage.mapper.StarMapper;
import club.chillman.roommanage.service.StarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author NIU
 * @createTime 2021/5/5 20:48
 */
@Service
public class StarServiceImpl extends ServiceImpl<StarMapper, Star> implements StarService {
}
