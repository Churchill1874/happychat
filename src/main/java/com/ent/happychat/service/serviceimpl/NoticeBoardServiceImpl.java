package com.ent.happychat.service.serviceimpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ent.happychat.entity.NoticeBoard;
import com.ent.happychat.mapper.NoticeBoardMapper;
import com.ent.happychat.service.NoticeBoardService;
import org.springframework.stereotype.Service;

@Service
public class NoticeBoardServiceImpl extends ServiceImpl<NoticeBoardMapper, NoticeBoard> implements NoticeBoardService {
}
