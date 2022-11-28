package com.bz.challenge.service.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriterion {

    private String key;
    private SearchOperation operation;
    private String value;

}


