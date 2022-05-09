package com.example.project.utility;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter
@Setter
@Data
public class Beautify<T> {

    private final Page<T> paginator;
    private final List<T> lstData;
    private final Map<String, Object> response;

    public Beautify(Page<T> paginator) {
        this.paginator = paginator;
        this.lstData = new ArrayList<>();
        this.response = new HashMap<>();
    }

    public Beautify(Page<T> paginator, List<T> lstData) {
        this.paginator = paginator;
        this.lstData = lstData;
        this.response = new HashMap<>();
    }

    public Map<String, Object> transformDataToJson() {
        response.put("data", lstData);
        response.put("currentPage", paginator.getNumber());
        response.put("totalItems", paginator.getTotalElements());
        response.put("totalPages", paginator.getTotalPages());
        return response;
    }

}
