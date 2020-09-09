package com.boot.store.dto.plugin;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @Author：chenaiwei
 * @Description：BatchDeleteModelDto
 * @CreateDate：2020/9/9 15:49
 */
@Data
public class BatchDoModelDto implements Serializable {
	@NotNull(message = "id不能为空！")
	private List<Long> idsList;
}
