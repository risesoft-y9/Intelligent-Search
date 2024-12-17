package net.risesoft.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AttachmentDTO  implements Serializable {

    private static final long serialVersionUID = -2992568584002492999L;

    /**
     * 附件名称
     */
    private String fileName;

    /**
     * 附件类型
     */
    private String fileType;

    /**
     * 附件URL
     */
    private String fileUrl;

}
