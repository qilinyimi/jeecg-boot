package org.jeecg.modules.demo.authority.vo;

import java.util.List;
import org.jeecg.modules.demo.authority.entity.AuthorityRuleTest;
import org.jeecg.modules.demo.authority.entity.AuthorityRuleTestSub;
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
 * @Description: 权限控制演示
 * @Author: jeecg-boot
 * @Date:   2022-08-25
 * @Version: V1.0
 */
@Data
@ApiModel(value="authority_rule_testPage对象", description="权限控制演示")
public class AuthorityRuleTestPage {

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
	/**部门*/
	@Excel(name = "部门", width = 15, dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    @Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
	@ApiModelProperty(value = "部门")
    private java.lang.String orgCode;
	/**姓名*/
	@Excel(name = "姓名", width = 15)
	@ApiModelProperty(value = "姓名")
    private java.lang.String userName;
	/**年龄*/
	@Excel(name = "年龄", width = 15)
	@ApiModelProperty(value = "年龄")
    private java.lang.Integer userAge;
	/**租户ID*/
	@Excel(name = "租户ID", width = 15)
	@ApiModelProperty(value = "租户ID")
    private java.lang.Integer tenantId;

	@ExcelCollection(name="权限控制演示子表")
	@ApiModelProperty(value = "权限控制演示子表")
	private List<AuthorityRuleTestSub> authorityRuleTestSubList;

}
