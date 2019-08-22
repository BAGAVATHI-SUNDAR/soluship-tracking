package com.soluship.tracking.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.soluship.tracking.bean.APIShipmentStatus;
import com.soluship.tracking.bean.OrderStatus;

@Mapper
public interface DBMapper {

	List<APIShipmentStatus> selectAPIShipmentStatusesByCarrierId(long carrierId);

	OrderStatus selectOrderStatusById(int orderStatusId);

}
