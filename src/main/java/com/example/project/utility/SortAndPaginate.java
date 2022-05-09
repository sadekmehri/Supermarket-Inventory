package com.example.project.utility;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;


@Data
@Getter
@Setter
public class SortAndPaginate<T> {

    private final int page;
    private final int size;
    private final String[] sort;
    private final List<Sort.Order> orders;
    private Page<T> paginator;
    private Pageable pagingSort;

    public SortAndPaginate(int page, int size, String[] sort) {
        this.page = page;
        this.size = size;
        this.sort = sort;
        this.paginator = null;
        this.pagingSort = null;
        this.orders = new ArrayList<>();
    }

    public SortAndPaginate(int page, int size) {
        this.page = 0;
        this.size = 10;
        this.sort = new String[]{"id,desc"};
        this.paginator = null;
        this.pagingSort = null;
        this.orders = new ArrayList<>();
    }

    public List<T> getSortedPaginatedList(Page<T> paginator) {
        this.paginator = paginator;
        List<T> lst = paginator.getContent();
        ListUtils.isListEmpty(lst);
        return lst;
    }

    public void listSortAttribute() {
        if (sort[0].contains(",")) {
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));
            }
        } else {
            orders.add(new Sort.Order(getSortDirection(sort[1]), sort[0]));
        }

        setPagingSort(PageRequest.of(page, size, Sort.by(orders)));
    }

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equalsIgnoreCase("desc"))
            return Sort.Direction.DESC;

        return Sort.Direction.ASC;
    }

}
