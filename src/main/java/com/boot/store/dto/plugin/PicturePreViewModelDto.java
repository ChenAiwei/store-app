package com.boot.store.dto.plugin;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author：chenaiwei
 * @Description：PicturePreViewModelDto
 * @CreateDate：2020/9/24 8:45
 *
 * {
 *   "title": "", //相册标题
 *   "id": 123, //相册id
 *   "start": 0, //初始显示的图片序号，默认0
 *   "data": [   //相册包含的图片，数组格式
 *     {
 *       "alt": "图片名",
 *       "pid": 666, //图片id
 *       "src": "", //原图地址
 *       "thumb": "" //缩略图地址
 *     }
 *   ]
 * }
 */
@Data
public class PicturePreViewModelDto implements Serializable {
	private String title;
	private Integer id;
	private Integer start;
	private List<PictureDataDto> data;

}

