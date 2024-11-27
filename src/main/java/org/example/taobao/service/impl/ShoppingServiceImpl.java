package org.example.taobao.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.taobao.mapper.ShoppingMapper;
import org.example.taobao.service.ShoppingService;
import org.example.taobao.utils.AliOssUtil;
import org.example.taobao.vo.PageVo;
import org.example.taobao.vo.Result;
import org.example.taobao.vo.ShoppingVo;
import org.ini4j.Ini;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.example.taobao.utils.AliOssUtil.upLoadFile;

/**
 * @author 关岁安
 */
@Service
public class ShoppingServiceImpl implements ShoppingService {


    @Autowired
    private ShoppingMapper shoppingMapper;

    @Override
    public Result registerShopping(MultipartFile file, String userId, String address, String username, String shoppingName, String type,String content)  {
        Integer id = Integer.valueOf(userId);
        String originalFileName = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + originalFileName.substring(originalFileName.lastIndexOf("."));
        try {
            String url = AliOssUtil.upLoadFile(fileName,file.getInputStream());
            shoppingMapper.registerShopping(id,shoppingName,type,url,address,username,content);
            return Result.success(url);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Result.error("服务器出现了问题");
        }

    }


    @Override
    public Result gainShops(Integer id,Integer page,Integer pageSize) {
        //1.设置分页查询的设置
        //参数1：页码：page
        //参数2：每页显示的个数：pageSize

        PageHelper.startPage(page,pageSize);
        //2.查询所有的数据
        List<ShoppingVo> noList = shoppingMapper.gainNoPassShop(id);
        System.out.println(noList);

        //3.把记录所有数据的集合转成Page类型,获取PageVo所需要的参数

        HashMap<String, PageVo> map = new HashMap<>();

        if(noList.isEmpty()){
            map.put("pageNo",null);
        }else{
            Page pNo =(Page) noList;
            long totalNo = pNo.getTotal();
            List<ShoppingVo> empListNo = pNo.getResult();
            PageVo pageVoNo = new PageVo(totalNo,empListNo);
            map.put("pageNo",pageVoNo);
        }

        PageVo pageVoYes = getYesList(id,page,pageSize);
        map.put("pageYes",pageVoYes);

        return Result.success(map);
    }

    @Override
    public Result gainNoShopsList(Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<ShoppingVo> noList = shoppingMapper.gainNoShopsList();
        if(noList.isEmpty()){
            return null;
        }else{
            Page pNo = (Page) noList;
            List<ShoppingVo> empListYes = pNo.getResult();
            long totalYes = pNo.getTotal();
            PageVo pageVoYes = new PageVo(totalYes,empListYes);
            return Result.success(pageVoYes);
        }
    }

    @Override
    public void changeShoppingHead(String url,Integer id) {
        shoppingMapper.changeShoppingHead(url,id);
    }

    @Override
    public Result gainShoppingBasicINformationBySpuId(Long spuId) {
        ShoppingVo shoppingVo = shoppingMapper.gainShopVoByShopId(spuId);
        return Result.success(shoppingVo);
    }

    public PageVo getYesList(Integer id,Integer page,Integer pageSize){
        PageHelper.startPage(page,pageSize);
        List<ShoppingVo> yesList = shoppingMapper.gainYesPassShop(id);
        System.out.println(yesList);
        if(yesList.isEmpty()){
//            map.put("pageYes",null);
            return null;
        }else{
            Page pYes = (Page) yesList;
            long totalYes = pYes.getTotal();
            List<ShoppingVo> empListYes = pYes.getResult();
            //创建PageVo对象 把总记录数和此页所有数据封装到PageVo对象中
            PageVo pageVoYes = new PageVo(totalYes,empListYes);
//            map.put("pageYes",pageVoYes);
            return pageVoYes;
        }
    }
}
