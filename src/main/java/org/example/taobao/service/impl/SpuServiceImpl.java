package org.example.taobao.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.taobao.dto.OnePriceDto;
import org.example.taobao.dto.PageCategoryDto;
import org.example.taobao.dto.SpuDto;
import org.example.taobao.mapper.AttributeMapper;
import org.example.taobao.mapper.SpuAttributeValueMapper;
import org.example.taobao.mapper.SpuImgMapper;
import org.example.taobao.mapper.SpuMapper;
import org.example.taobao.pojo.BasicAttribute;
import org.example.taobao.service.SpuService;
import org.example.taobao.vo.GoodsVo;
import org.example.taobao.vo.PageVo;
import org.example.taobao.vo.SimplyGoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.example.taobao.utils.IdGenerationTest.generateId;
import static org.example.taobao.utils.UploadImgToAlibaba.uploadImgToAlibaba;

/**
 * @author 关岁安
 */

@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private AttributeMapper attributeMapper;

    @Autowired
    private SpuAttributeValueMapper spuAttributeValueMapper;

    @Autowired
    private SpuImgMapper spuImgMapper;

    /**
     * 1.先插spu表
     * 2.再插基本属性表
     * 3.最后再插属性价值表
     * @param spuDto
     */
    @Override
    public Long uploadSpu(SpuDto spuDto) {
        Integer categoryId =  spuDto.getCategoryId();
        String spuName = spuDto.getSpuName();
        String title = spuDto.getTitle();
        List<BasicAttribute> basicAttributes = Arrays.asList(spuDto.getBasicAttributes());
        Integer shoppingId = spuDto.getShoppingId();

        Long id = generateId();
        LocalDateTime currentTime = LocalDateTime.now();
        if(spuDto.getIs() == 1){
            spuMapper.insertIsSpu(id,spuName,title,categoryId,currentTime,shoppingId);
        }else if(spuDto.getIs() == 0){
            spuMapper.insertSpu(id,spuName,title,categoryId,currentTime,shoppingId);
        }
        insertBasicAttribute(basicAttributes,categoryId,id);
        return id;
    }

    @Override
    public void uploadSpuImg(MultipartFile file,Integer spuId) throws Exception {
        String url = uploadImgToAlibaba(file);
        spuImgMapper.uploadSpuImg(url,spuId);
    }

    @Override
    public void uploadSpuMainImg(MultipartFile file, Integer spuId) throws Exception {
        String url = uploadImgToAlibaba(file);
        spuMapper.uploadSpuMainImg(url,spuId);
    }

    @Override
    public void uploadOnePrice(OnePriceDto onePriceDto) {
        String onePrice = onePriceDto.getOnePrice();
        Long id  = onePriceDto.getSpuId();
        spuMapper.uploadOnePrice(onePrice,id);
    }

    @Override
    public PageVo gainSimplyGoodsVoList(Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<SimplyGoodsVo> list = spuMapper.gainSimplyList();
        System.out.println(list);
        Page pageList = (Page) list;
        long totalPageList = pageList.getTotal();
        List<SimplyGoodsVo> simplyGoodsVos = pageList.getResult();
        PageVo pageVo = new PageVo(totalPageList,simplyGoodsVos);
        return pageVo;
    }

    @Override
    public GoodsVo gainGoodsById(Integer id) {
        return spuMapper.gainGoods(id);
    }

    @Override
    public List<String> gainSpuImgs(Integer id) {
        return spuImgMapper.gainSpuImgs(id);
    }

    @Override
    public PageVo gainCategoryGoods(PageCategoryDto pageCategoryDto) {


        PageHelper.startPage(pageCategoryDto.getPage(),pageCategoryDto.getPageSize());
        List<SimplyGoodsVo> list = spuMapper.gainSeckillerGoods(pageCategoryDto);
        System.out.println(list);
        Page pageList = (Page) list;
        long totalPageList = pageList.getTotal();
        List<SimplyGoodsVo> simplyGoodsVos = pageList.getResult();
        PageVo pageVo = new PageVo(totalPageList,simplyGoodsVos);

        return pageVo;
    }


    public void insertBasicAttribute(List<BasicAttribute> basicAttributes, Integer categoryId,Long spuId){
        for (BasicAttribute basicAttribute : basicAttributes) {
            LocalDateTime currentTime = LocalDateTime.now();
            Long id = generateId();
            String name = basicAttribute.getName();
            String value = basicAttribute.getValue();
            attributeMapper.insertBasicAttribute(id,name,categoryId,currentTime,spuId);
            spuAttributeValueMapper.insertBasicAttributeValue(spuId,id,value);
        }
    }


}
