package fun.asgc.neutrino.proxy.server.controller.req.report;

import lombok.Data;

/**
 * @author: aoshiguchen
 * @date: 2022/12/21
 */
@Data
public class LicenseFlowReportReq {
    private Integer userId;
    private Integer licenseId;
}