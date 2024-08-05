package com.ent.happychat.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ent.happychat.entity.PlayerInfo;
import com.ent.happychat.entity.ViewsRecord;
import com.ent.happychat.pojo.req.views.ViewsRecordPageReq;
import com.ent.happychat.pojo.resp.views.ViewsRecordPageResp;
import com.ent.happychat.service.PlayerInfoService;
import com.ent.happychat.service.ViewsRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Api(tags = "浏览记录")
@RequestMapping("/manage/views")
public class ViewsRecordController {

    @Autowired
    private ViewsRecordService viewsRecordService;
    @Autowired
    private PlayerInfoService playerInfoService;

    @PostMapping("/queryPage")
    @ApiOperation(value = "分页", notes = "分页")
    public R<IPage<ViewsRecordPageResp>> queryPage(@RequestBody @Valid ViewsRecordPageReq req) {

        IPage<ViewsRecordPageResp> viewsRecordPageRespPage = new Page<>(req.getPageNum(), req.getPageSize());

        IPage<ViewsRecord> iPage = viewsRecordService.queryPage(req);
        if (CollectionUtils.isEmpty(iPage.getRecords())){
            return R.ok(viewsRecordPageRespPage);
        }

        List<Long> playerIdList = iPage.getRecords().stream().map(ViewsRecord::getPlayerId).collect(Collectors.toList());
        Map<Long, PlayerInfo> playerInfoMap = playerInfoService.mapByIds(playerIdList);

        List<ViewsRecordPageResp> viewsRecordPageRespList = new ArrayList<>();
        iPage.getRecords().forEach(viewsRecord -> {
            PlayerInfo playerInfo = playerInfoMap.get(viewsRecord.getPlayerId());
            ViewsRecordPageResp viewsRecordPageResp = BeanUtil.toBean(viewsRecord, ViewsRecordPageResp.class);
            viewsRecordPageResp.setAccount(playerInfo.getAccount());
            viewsRecordPageResp.setLevel(playerInfo.getLevel());
            viewsRecordPageResp.setPlayerName(playerInfo.getName());
            viewsRecordPageRespList.add(viewsRecordPageResp);
        });

        viewsRecordPageRespPage.setRecords(viewsRecordPageRespList);
        return R.ok(viewsRecordPageRespPage);
    }

}
