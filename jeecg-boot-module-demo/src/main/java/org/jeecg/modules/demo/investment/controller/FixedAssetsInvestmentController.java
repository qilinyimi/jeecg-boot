package org.jeecg.modules.demo.investment.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xkcoding.http.util.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.demo.investment.entity.FixedAssetsInvestment;
import org.jeecg.modules.demo.investment.entity.FixedAssetsInvestmentLog;
import org.jeecg.modules.demo.investment.entity.FixedAssetsInvestmentSub;
import org.jeecg.modules.demo.investment.service.IFixedAssetsInvestmentLogService;
import org.jeecg.modules.demo.investment.service.IFixedAssetsInvestmentService;
import org.jeecg.modules.demo.investment.service.IFixedAssetsInvestmentSubService;
import org.jeecg.modules.demo.investment.vo.FixedAssetsInvestmentPage;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

 /**
 * @Description: ?????????????????????
 * @Author: jeecg-boot
 * @Date:   2022-08-11
 * @Version: V1.0
 */
@Api(tags="?????????????????????")
@RestController
@RequestMapping("/investment/fixedAssetsInvestment")
@Slf4j
public class FixedAssetsInvestmentController {
	@Autowired
	private IFixedAssetsInvestmentService fixedAssetsInvestmentService;
	@Autowired
	private IFixedAssetsInvestmentSubService fixedAssetsInvestmentSubService;
	@Autowired
	private IFixedAssetsInvestmentLogService fixedAssetsInvestmentLogService;
	
	/**
	 * ??????????????????
	 *
	 * @param fixedAssetsInvestment
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "?????????????????????-??????????????????")
	@ApiOperation(value="?????????????????????-??????????????????", notes="?????????????????????-??????????????????")
	@GetMapping(value = "/list")
	public Result<IPage<FixedAssetsInvestment>> queryPageList(FixedAssetsInvestment fixedAssetsInvestment,
								   @RequestParam(required = false,name="curSysOrgCode")  String curSysOrgCode,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		//??????????????????????????????
		fixedAssetsInvestment.setProjectStatus(0);
		QueryWrapper<FixedAssetsInvestment> queryWrapper = QueryGenerator.initQueryWrapper(fixedAssetsInvestment, req.getParameterMap());
		//??????????????????
		if(!StringUtil.isEmpty(curSysOrgCode)){
			queryWrapper.likeRight("sys_org_code",curSysOrgCode);
		}
		Page<FixedAssetsInvestment> page = new Page<FixedAssetsInvestment>(pageNo, pageSize);
		IPage<FixedAssetsInvestment> pageList = fixedAssetsInvestmentService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  * ??????????????????
	  *
	  * @param fixedAssetsInvestment
	  * @param pageNo
	  * @param pageSize
	  * @param req
	  * @return
	  */
	 //@AutoLog(value = "?????????????????????-??????????????????")
	 @ApiOperation(value="??????????????????????????????-??????????????????", notes="?????????????????????-??????????????????")
	 @GetMapping(value = "/listInitiation")
	 public Result<IPage<FixedAssetsInvestment>> listInitiation(FixedAssetsInvestment fixedAssetsInvestment,
															   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
															   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
															   HttpServletRequest req) {
		 QueryWrapper<FixedAssetsInvestment> queryWrapper = QueryGenerator.initQueryWrapper(fixedAssetsInvestment, req.getParameterMap());
		 queryWrapper.ge("project_status",1);
		 Page<FixedAssetsInvestment> page = new Page<FixedAssetsInvestment>(pageNo, pageSize);
		 IPage<FixedAssetsInvestment> pageList = fixedAssetsInvestmentService.page(page, queryWrapper);
		 return Result.OK(pageList);
	 }
	 /**
	  *  ??????
	  *
	  * @param projectId ??????Id
	  * @return
	  */
	 @AutoLog(value = "?????????????????????-??????")
	 @ApiOperation(value="?????????????????????-??????", notes="?????????????????????-??????")
	 @RequestMapping(value = "/initiation", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> initiation(@RequestParam() String projectId) {
		 FixedAssetsInvestment fixedAssetsInvestmentEntity = fixedAssetsInvestmentService.getById(projectId);
		 if(fixedAssetsInvestmentEntity==null) {
			 return Result.error("?????????????????????");
		 }
		 fixedAssetsInvestmentService.initiation(fixedAssetsInvestmentEntity);
		 return Result.OK("????????????!");
	 }
	 /**
	  *  ????????????
	  *
	  * @param projectId ??????Id
	  * @return
	  */
	 @AutoLog(value = "?????????????????????-????????????")
	 @ApiOperation(value="?????????????????????-????????????", notes="?????????????????????-????????????")
	 @RequestMapping(value = "/initiationSubmit", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> initiationSubmit(@RequestParam() String projectId) {
		 FixedAssetsInvestment fixedAssetsInvestmentEntity = fixedAssetsInvestmentService.getById(projectId);
		 if(fixedAssetsInvestmentEntity==null) {
			 return Result.error("?????????????????????");
		 }
		 fixedAssetsInvestmentService.initiationSubmit(fixedAssetsInvestmentEntity);
		 return Result.OK("????????????!");
	 }



	 /**
	 *   ??????
	 *
	 * @param fixedAssetsInvestmentPage
	 * @return
	 */
	@AutoLog(value = "?????????????????????-??????")
	@ApiOperation(value="?????????????????????-??????", notes="?????????????????????-??????")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody FixedAssetsInvestmentPage fixedAssetsInvestmentPage) {
		FixedAssetsInvestment fixedAssetsInvestment = new FixedAssetsInvestment();
		BeanUtils.copyProperties(fixedAssetsInvestmentPage, fixedAssetsInvestment);
		fixedAssetsInvestmentService.saveMain(fixedAssetsInvestment, fixedAssetsInvestmentPage.getFixedAssetsInvestmentSubList(),fixedAssetsInvestmentPage.getFixedAssetsInvestmentLogList());
		return Result.OK("???????????????");
	}
	
	/**
	 *  ??????
	 *
	 * @param fixedAssetsInvestmentPage
	 * @return
	 */
	@AutoLog(value = "?????????????????????-??????")
	@ApiOperation(value="?????????????????????-??????", notes="?????????????????????-??????")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody FixedAssetsInvestmentPage fixedAssetsInvestmentPage) {
		FixedAssetsInvestment fixedAssetsInvestment = new FixedAssetsInvestment();
		BeanUtils.copyProperties(fixedAssetsInvestmentPage, fixedAssetsInvestment);
		FixedAssetsInvestment fixedAssetsInvestmentEntity = fixedAssetsInvestmentService.getById(fixedAssetsInvestment.getId());
		if(fixedAssetsInvestmentEntity==null) {
			return Result.error("?????????????????????");
		}
		fixedAssetsInvestmentService.updateMain(fixedAssetsInvestment, fixedAssetsInvestmentPage.getFixedAssetsInvestmentSubList(),fixedAssetsInvestmentPage.getFixedAssetsInvestmentNegativeList());
		return Result.OK("????????????!");
	}
	
	/**
	 *   ??????id??????
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "?????????????????????-??????id??????")
	@ApiOperation(value="?????????????????????-??????id??????", notes="?????????????????????-??????id??????")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		fixedAssetsInvestmentService.delMain(id);
		return Result.OK("????????????!");
	}
	
	/**
	 *  ????????????
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "?????????????????????-????????????")
	@ApiOperation(value="?????????????????????-????????????", notes="?????????????????????-????????????")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.fixedAssetsInvestmentService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("?????????????????????");
	}
	
	/**
	 * ??????id??????
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "?????????????????????-??????id??????")
	@ApiOperation(value="?????????????????????-??????id??????", notes="?????????????????????-??????id??????")
	@GetMapping(value = "/queryById")
	public Result<FixedAssetsInvestment> queryById(@RequestParam(name="id",required=true) String id) {
		FixedAssetsInvestment fixedAssetsInvestment = fixedAssetsInvestmentService.getById(id);
		if(fixedAssetsInvestment==null) {
			return Result.error("?????????????????????");
		}
		return Result.OK(fixedAssetsInvestment);

	}
	
	/**
	 * ??????id??????
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "????????????????????????ID??????")
	@ApiOperation(value="??????????????????ID??????", notes="????????????-?????????ID??????")
	@GetMapping(value = "/queryFixedAssetsInvestmentSubByMainId")
	public Result<List<FixedAssetsInvestmentSub>> queryFixedAssetsInvestmentSubListByMainId(@RequestParam(name="id",required=true) String id) {
		List<FixedAssetsInvestmentSub> fixedAssetsInvestmentSubList = fixedAssetsInvestmentSubService.selectByMainId(id);
		return Result.OK(fixedAssetsInvestmentSubList);
	}
	/**
	 * ??????id??????
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "???????????????????????????????????????ID??????")
	@ApiOperation(value="?????????????????????????????????ID??????", notes="???????????????????????????-?????????ID??????")
	@GetMapping(value = "/queryFixedAssetsInvestmentLogByMainId")
	public Result<List<FixedAssetsInvestmentLog>> queryFixedAssetsInvestmentLogListByMainId(@RequestParam(name="id",required=true) String id) {
		List<FixedAssetsInvestmentLog> fixedAssetsInvestmentLogList = fixedAssetsInvestmentLogService.selectByMainId(id);
		return Result.OK(fixedAssetsInvestmentLogList);
	}

    /**
    * ??????excel
    *
    * @param request
    * @param fixedAssetsInvestment
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, FixedAssetsInvestment fixedAssetsInvestment) {
      // Step.1 ??????????????????????????????
      QueryWrapper<FixedAssetsInvestment> queryWrapper = QueryGenerator.initQueryWrapper(fixedAssetsInvestment, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //??????????????????????????????
       String selections = request.getParameter("selections");
       if(oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            queryWrapper.in("id",selectionList);
       }
       //Step.2 ??????????????????
       List<FixedAssetsInvestment> fixedAssetsInvestmentList = fixedAssetsInvestmentService.list(queryWrapper);

      // Step.3 ??????pageList
      List<FixedAssetsInvestmentPage> pageList = new ArrayList<FixedAssetsInvestmentPage>();
      for (FixedAssetsInvestment main : fixedAssetsInvestmentList) {
          FixedAssetsInvestmentPage vo = new FixedAssetsInvestmentPage();
          BeanUtils.copyProperties(main, vo);
          List<FixedAssetsInvestmentSub> fixedAssetsInvestmentSubList = fixedAssetsInvestmentSubService.selectByMainId(main.getId());
          vo.setFixedAssetsInvestmentSubList(fixedAssetsInvestmentSubList);
          List<FixedAssetsInvestmentLog> fixedAssetsInvestmentLogList = fixedAssetsInvestmentLogService.selectByMainId(main.getId());
          vo.setFixedAssetsInvestmentLogList(fixedAssetsInvestmentLogList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi ??????Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "???????????????????????????");
      mv.addObject(NormalExcelConstants.CLASS, FixedAssetsInvestmentPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("???????????????????????????", "?????????:"+sysUser.getRealname(), "?????????????????????"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
		String field = request.getParameter("field");
		if(oConvertUtils.isNotEmpty(field)){
			 mv.addObject("exportFields",field);
		}
      return mv;
    }

    /**
    * ??????excel????????????
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          // ????????????????????????
          MultipartFile file = entity.getValue();
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<FixedAssetsInvestmentPage> list = ExcelImportUtil.importExcel(file.getInputStream(), FixedAssetsInvestmentPage.class, params);
              for (FixedAssetsInvestmentPage page : list) {
                  FixedAssetsInvestment po = new FixedAssetsInvestment();
                  BeanUtils.copyProperties(page, po);
                  fixedAssetsInvestmentService.saveMain(po, page.getFixedAssetsInvestmentSubList(),page.getFixedAssetsInvestmentLogList());
              }
              return Result.OK("?????????????????????????????????:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("??????????????????:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.OK("?????????????????????");
    }

}
