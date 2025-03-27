package com.example.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 文件上传记录
 */

@Data
public class FileEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private String fileId;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 附件类型
     */

    private String fileType;
    /**
     * 附件分类（1：招标文件2其它澄清承诺材料3：合同4：公示）
     */
    private String typeId;
    /**
     * FaBaoGuid
     */
    private String prjId;
    /**
     * BiaoDuanGuid
     */
    private String sectId;
    /**
     * 文件大小
     */
    private Float fileSize;


    /**
     * 备注
     */
    private String remark;
    /**
     * 文件二进制数据,base64
     */
    private String fileDataBase64;

}
