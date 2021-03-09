package at.ac.tuwien.rest.addressbook.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.json.bind.annotation.JsonbProperty;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmploymentDTO {

    private String function;

    @JsonbProperty("org_ref")
    private OrganisationalUnit organisationalUnit;
}