package com.boot.store.dto.plugin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.ConstructorArgs;

import java.io.Serializable;

/**
 * @Author：chenaiwei
 * @Description：XmSelectModelDto
 * @CreateDate：2020/9/8 17:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XmSelectModelDto implements Serializable {
	private String name;
	private Long value;
}
