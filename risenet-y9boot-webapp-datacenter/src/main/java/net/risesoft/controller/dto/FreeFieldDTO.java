package net.risesoft.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FreeFieldDTO  implements Serializable {

    private static final long serialVersionUID = -2992568584112492961L;

    /**
     * 字段名称
     */
    private String name;

    /**
     * 字段内容
     */
    private String content;

}
