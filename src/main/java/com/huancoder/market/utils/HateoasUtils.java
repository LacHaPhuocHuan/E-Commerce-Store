package com.huancoder.market.utils;

import com.huancoder.market.dto.HateoasObject;
import com.huancoder.market.dto.common.APIRelEnum;
import com.huancoder.market.security.Role;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

public class HateoasUtils {
    //1.Class main
    //2.list enum value method.
    //3.class 2
    //4. list: (method 2=>??slash()->)
    //5. ROLE:
//    public static  <T> List<EntityModel<T>> convertToHateoas(
//            Class<?> bClass, HashMap<APIRelEnum, String> relSlash,
//            Role role, List<HateoasObject> objects
//            ){
//        List<EntityModel<T>> entityModels= objects.stream().map(o->{
//            EntityModel<T> entityModel= (EntityModel<T>) EntityModel.of((T)o.getObject());
//            entityModel=convertEntityModelRel(entityModel,bClass, relSlash, o.getSlash());
//            return entityModel;
//        }).collect(Collectors.toList());
//        return entityModels;
//
//    }

//    private static <T> EntityModel<T> convertEntityModelMain(EntityModel<T> entityModel, Class<?> aClass, HashMap<APIRelEnum,Long> apiRelEnumMap) {
//        Set<APIRelEnum> apiRelEnums= apiRelEnumMap.keySet();
//        for (APIRelEnum relEnum: apiRelEnums
//             ) {
//            if(relEnum.equals(APIRelEnum.ADD)||relEnum.equals(APIRelEnum.GETS))
//                entityModel=addNonSlashLink(entityModel,aClass,relEnum);
//            else
//                entityModel=addSlashLink(entityModel,aClass,apiRelEnumMap.get(APIRelEnum.DELETE), relEnum);
//
//        }
//        return  entityModel;
//    }

    public static <T> EntityModel<T> addSlashLink(EntityModel<T> entityModel, Class<?> aClass, String slash, String rel) {
        entityModel.add(WebMvcLinkBuilder.linkTo(aClass).slash(slash).withRel(rel));
        return entityModel;
    }

    public static <T> EntityModel<T> addNonSlashLink(EntityModel<T> entityModel, Class<?> aClass, String rel) {
        entityModel.add(WebMvcLinkBuilder.linkTo(aClass).withRel(rel));
        return entityModel;
    }


    public static <T> EntityModel<T> convertEntityModelRelBase(EntityModel<T> entityModel, Class<?> bClass, String slash) {
        entityModel=addSlashLink(entityModel,bClass,slash,"update");
        entityModel=addSlashLink(entityModel,bClass,slash,"delete");
        entityModel=addSlashLink(entityModel,bClass,slash,"get");
        entityModel=addNonSlashLink(entityModel,bClass,"add");
        return  entityModel;
    }
    public static <T> EntityModel<T> convertEntityModelRelForGetOneObjects(EntityModel<T> entityModel, Class<?> bClass, String slash) {
        entityModel=addSlashLink(entityModel,bClass,slash,"update");
        entityModel=addSlashLink(entityModel,bClass,slash,"delete");
        entityModel=addNonSlashLink(entityModel,bClass,"gets");
        entityModel=addNonSlashLink(entityModel,bClass,"add");
        return  entityModel;
    }




}
