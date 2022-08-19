package org.jeecg.modules.demo.report.vo;

import java.util.List;
import org.jeecg.modules.demo.report.entity.InvestPlanReport;
import org.jeecg.modules.demo.report.entity.InvestPlanRelationDetail;
import org.jeecg.modules.demo.report.entity.InvestPlanSubRelationDetail;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 投资计划上报
 * @Author: jeecg-boot
 * @Date:   2022-08-19
 * @Version: V1.0
 */
@Data
@ApiModel(value="invest_plan_reportPage对象", description="投资计划上报")
public class InvestPlanReportPage {

	/**主键*/
	@ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
	@ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
	@ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
	@ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
	/**状态名称*/
	@Excel(name = "状态名称", width = 15)
	@ApiModelProperty(value = "状态名称")
    private java.lang.String stateName;
	/**状态编码*/
	@Excel(name = "状态编码", width = 15)
	@ApiModelProperty(value = "状态编码")
    private java.lang.String stateCode;
	/**编号*/
	@Excel(name = "编号", width = 15)
	@ApiModelProperty(value = "编号")
    private java.lang.String code;
	/**上报单位*/
	@Excel(name = "上报单位", width = 15)
	@ApiModelProperty(value = "上报单位")
    private java.lang.String reportUnitName;
	/**上报年度*/
	@Excel(name = "上报年度", width = 15)
	@ApiModelProperty(value = "上报年度")
    private java.lang.Integer year;
	/**版本号*/
	@Excel(name = "版本号", width = 15)
	@ApiModelProperty(value = "版本号")
    private java.lang.String versionNo;
	/**项目数*/
	@Excel(name = "项目数", width = 15)
	@ApiModelProperty(value = "项目数")
    private java.lang.Integer projectCount;
	/**提交人*/
	@Excel(name = "提交人", width = 15)
	@ApiModelProperty(value = "提交人")
    private java.lang.String submitterId;
	/**提交人Name*/
	@Excel(name = "提交人Name", width = 15)
	@ApiModelProperty(value = "提交人Name")
    private java.lang.String submitterName;
	/**提交时间*/
	@Excel(name = "提交时间", width = 15)
	@ApiModelProperty(value = "提交时间")
    private java.lang.String submitTime;
	/**本级投资总资金*/
	@Excel(name = "本级投资总资金", width = 15)
	@ApiModelProperty(value = "本级投资总资金")
    private java.math.BigDecimal currentAmount;
	/**下级投资总资金*/
	@Excel(name = "下级投资总资金", width = 15)
	@ApiModelProperty(value = "下级投资总资金")
    private java.math.BigDecimal lowerAmount;
	/**投资总资金*/
	@Excel(name = "投资总资金", width = 15)
	@ApiModelProperty(value = "投资总资金")
    private java.math.BigDecimal totalAmount;
	/**登记人ID*/
	@Excel(name = "登记人ID", width = 15)
	@ApiModelProperty(value = "登记人ID")
    private java.lang.String creatorId;
	/**登记人Name*/
	@Excel(name = "登记人Name", width = 15)
	@ApiModelProperty(value = "登记人Name")
    private java.lang.String creatorName;
	/**修改人ID*/
	@Excel(name = "修改人ID", width = 15)
	@ApiModelProperty(value = "修改人ID")
    private java.lang.String modifierId;
	/**修改人Name*/
	@Excel(name = "修改人Name", width = 15)
	@ApiModelProperty(value = "修改人Name")
    private java.lang.String modifierName;

	@ExcelCollection(name="投资计划关联年度投资计划")
	@ApiModelProperty(value = "投资计划关联年度投资计划")
	private List<InvestPlanRelationDetail> investPlanRelationDetailList;
	@ExcelCollection(name="关联下级年度投资计划")
	@ApiModelProperty(value = "关联下级年度投资计划")
	private List<InvestPlanSubRelationDetail> investPlanSubRelationDetailList;

}
