package com.example.project.service;

import com.example.project.entity.Order;
import com.example.project.entity.Staff;
import com.example.project.entity.Store;
import com.example.project.exception.ApiRequestException;
import com.example.project.repository.OrderRepository;
import com.example.project.repository.StaffRepository;
import com.example.project.repository.StoreRepository;
import com.example.project.utility.Beautify;
import com.example.project.utility.ListUtils;
import com.example.project.utility.OptionalUtils;
import com.example.project.utility.SortAndPaginate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final StaffRepository staffRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public StoreService(StoreRepository storeRepository, StaffRepository staffRepository, OrderRepository orderRepository) {
        this.storeRepository = storeRepository;
        this.staffRepository = staffRepository;
        this.orderRepository = orderRepository;
    }

    /* Get list of stores */
    public List<Store> getStores() {
        List<Store> lstStores = storeRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        ListUtils.isListEmpty(lstStores);

        return lstStores;
    }

    /* Get list of staff by store, sort and paginate them */
    public Map<String, Object> getAllStaffsByStore(int storeId, int page, int size, String[] sort) {
        SortAndPaginate<Staff> sp = new SortAndPaginate<>(page, size, sort);
        sp.listSortAttribute();

        List<Staff> lstStaffs = sp.getSortedPaginatedList(
                staffRepository.findByStoreId(storeId, sp.getPagingSort())
        );

        return new Beautify<>(sp.getPaginator(), lstStaffs).transformDataToJson();
    }

    /* Get list of orders by store, sort and paginate them */
    public Map<String, Object> getAllOrdersByStore(int storeId, int page, int size, String[] sort) {
        SortAndPaginate<Order> sp = new SortAndPaginate<>(page, size, sort);
        sp.listSortAttribute();

        List<Order> lstOrders = sp.getSortedPaginatedList(
                orderRepository.findByStoreId(storeId, sp.getPagingSort())
        );

        return new Beautify<>(sp.getPaginator(), lstOrders).transformDataToJson();
    }

    /* Get specific store by id */
    public Store getStore(int id) {
        return storeRepository.findById(id).orElseThrow(() -> {
            throw new ApiRequestException("Store with id " + id + " does not exist", HttpStatus.NOT_FOUND);
        });
    }

    /* Create new store */
    public Store createStore(Store store) {
        isStoreExistByCompanyName(store.getName());
        isStoreExistByPhone(store.getPhone());

        return storeRepository.save(store);
    }

    /* Update specific store */
    public Store updateStore(int id, Store newStore) {
        Store oldStore = getStore(id);

        isStoreNameExistWhenUpdating(id, newStore.getName());
        isStorePhoneExistWhenUpdating(id, newStore.getPhone());

        oldStore.setName(newStore.getName());
        oldStore.setPhone(newStore.getPhone());

        return storeRepository.save(oldStore);
    }

    /* Delete specific store */
    public void deleteStore(int id) {
        isStoreExistById(id);

        storeRepository.deleteById(id);
    }

    /*
     *
     *
     * Utils
     *
     *
     * */

    /* Check if the store exist filtering by id */
    private void isStoreExistById(int storeId) {
        boolean isExist = storeRepository.existsById(storeId);
        if (!isExist)
            throw new ApiRequestException("Store with id " + storeId + " does not exist", HttpStatus.NOT_FOUND);
    }

    /* Check if the store exists with the given name */
    private void isStoreExistByCompanyName(String companyName) {
        Optional<Store> store = storeRepository.findStoreByName(companyName);
        OptionalUtils.isPresent(store, "Store with name " + companyName + " already exists");
    }

    /* Check if the store exists with the given phone number */
    private void isStoreExistByPhone(String phoneNumber) {
        Optional<Store> store = storeRepository.findStoreByPhone(phoneNumber);
        OptionalUtils.isPresent(store, "Store with phone number " + phoneNumber + " already exists");
    }

    /* Check if the store exists with the given name when updating the store name */
    private void isStoreNameExistWhenUpdating(int id, String storeName) {
        Optional<Store> store = storeRepository.findStoreByNameToUpdate(id, storeName);
        OptionalUtils.isPresent(store, "Store with name " + storeName + " already exists");
    }

    /* Check if the store exists with the given phone number when updating the store phone number */
    private void isStorePhoneExistWhenUpdating(int id, String phoneNumber) {
        Optional<Store> store = storeRepository.findStoreByPhoneToUpdate(id, phoneNumber);
        OptionalUtils.isPresent(store, "Store with phone number " + phoneNumber + " already exists");
    }

}
