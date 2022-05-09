package com.example.project.service;

import com.example.project.entity.Order;
import com.example.project.entity.Staff;
import com.example.project.entity.Store;
import com.example.project.exception.ApiRequestException;
import com.example.project.repository.OrderRepository;
import com.example.project.repository.StaffRepository;
import com.example.project.repository.StoreRepository;
import com.example.project.utility.Beautify;
import com.example.project.utility.OptionalUtils;
import com.example.project.utility.SortAndPaginate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StaffService {

    private final StaffRepository staffRepository;
    private final StoreRepository storeRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public StaffService(StaffRepository staffRepository, StoreRepository storeRepository, OrderRepository orderRepository) {
        this.staffRepository = staffRepository;
        this.storeRepository = storeRepository;
        this.orderRepository = orderRepository;
    }

    /* Get list of staff, sort and paginate them */
    public Map<String, Object> getStaffs(int page, int size, String[] sort) {
        SortAndPaginate<Staff> sp = new SortAndPaginate<>(page, size, sort);
        sp.listSortAttribute();

        List<Staff> lstStaffs = sp.getSortedPaginatedList(
                staffRepository.findAll(sp.getPagingSort())
        );

        return new Beautify<>(sp.getPaginator(), lstStaffs).transformDataToJson();
    }

    /* Get list of orders by staff, sort and paginate them */
    public Map<String, Object> getOrdersByStaff(int staffId, int page, int size, String[] sort) {
        SortAndPaginate<Order> sp = new SortAndPaginate<>(page, size, sort);
        sp.listSortAttribute();

        List<Order> lstOrders = sp.getSortedPaginatedList(
                orderRepository.findByStaffId(staffId, sp.getPagingSort())
        );

        return new Beautify<>(sp.getPaginator(), lstOrders).transformDataToJson();
    }


    /* Get staff by id */
    public Staff getStaff(int id) {
        return staffRepository.findById(id).orElseThrow(() -> {
            throw new ApiRequestException("Staff with id " + id + " does not exist", HttpStatus.NOT_FOUND);
        });
    }

    /* Create new staff */
    public Staff createStaff(Staff staff) {
        Store store = staff.getStore();
        isStoreNull(store);
        isValidHireDate(staff);
        isStaffExistByPhone(staff.getPhone());

        store = isStoreExist(store.getId());
        staff.setStore(store);

        return staffRepository.save(staff);
    }

    /* Update existing staff */
    public Staff updateStaff(int id, Staff newStaff) {
        Staff oldStaff = getStaff(id);

        Store store = newStaff.getStore();
        isStoreNull(store);
        store = isStoreExist(store.getId());
        isValidHireDate(newStaff);
        isStaffPhoneExistWhenUpdating(id, newStaff.getPhone());

        oldStaff.setLastName(newStaff.getFirstName());
        oldStaff.setLastName(newStaff.getLastName());
        oldStaff.setPhone(newStaff.getPhone());
        oldStaff.setDob(newStaff.getDob());
        oldStaff.setHireDate(newStaff.getHireDate());
        oldStaff.setStore(store);

        return staffRepository.save(oldStaff);
    }

    /* Delete existing staff */
    public void deleteStaff(int id) {
        isStaffExistById(id);

        staffRepository.deleteById(id);
    }

    /*
     *
     *
     * Utils
     *
     *
     * */

    /* Check if the staff exist filtering by id */
    private void isStaffExistById(int staffId) {
        boolean isExist = staffRepository.existsById(staffId);
        if (!isExist)
            throw new ApiRequestException("Staff with id " + staffId + " does not exist", HttpStatus.NOT_FOUND);
    }

    /* Check if the staff store is null */
    private void isStoreNull(Store store) {
        if (store == null)
            throw new ApiRequestException("Not data found for store with the given id", HttpStatus.NOT_FOUND);
    }

    /* Check if the store id exists */
    private Store isStoreExist(int id) {
        return storeRepository.findById(id).orElseThrow(() -> {
            throw new ApiRequestException("Store with id " + id + " does not exist", HttpStatus.NOT_FOUND);
        });
    }

    /* Check if the current staff hire date validity */
    private void isValidHireDate(Staff staff) {
        if (!staff.isValidHireDate())
            throw new ApiRequestException("Hire date should be at least 12 years after his birth", HttpStatus.NOT_FOUND);
    }

    /* Check if the current staff phone number exists */
    private void isStaffExistByPhone(String phone) {
        Optional<Staff> emp = staffRepository.findStaffByPhone(phone);
        OptionalUtils.isPresent(emp, "Staff with phone " + phone + " already exists");
    }

    /* Check if the staff phone with the given phone when updating the staff phone */
    private void isStaffPhoneExistWhenUpdating(int id, String phone) {
        Optional<Staff> staff = staffRepository.findStaffByPhoneToUpdate(id, phone);
        OptionalUtils.isPresent(staff, "Staff with phone " + phone + " already exists");
    }

}
