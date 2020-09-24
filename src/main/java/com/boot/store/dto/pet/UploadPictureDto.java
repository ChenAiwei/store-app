package com.boot.store.dto.pet;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author：chenaiwei
 * @Description：UploadPictureDto
 * @CreateDate：2020/9/23 19:51
 */
@Data
public class UploadPictureDto implements Serializable {
	@NotNull(message = "id 不允许为空！")
	private Long id;
	@NotNull(message = "fileName 不允许为空！")
	private String fileName;
	@NotNull(message = "picStr 不允许为空！")
	private String picStr;
}
