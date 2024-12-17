package net.risesoft.util;

import net.risesoft.controller.dto.ArticleDTO;
import net.risesoft.controller.dto.AttachmentDTO;
import net.risesoft.controller.dto.FreeFieldDTO;
import net.risesoft.nosql.elastic.entity.Article.ArticleInfo;
import net.risesoft.nosql.elastic.entity.Article.Attachment;
import net.risesoft.nosql.elastic.entity.Article.FreeField;

import java.util.ArrayList;
import java.util.List;

public class CopyUtil {

	public static void articleInfoToArticleDTO(ArticleInfo article, ArticleDTO articleDTO) {
		articleDTO.setId(article.getId());
		articleDTO.setDataType(article.getDataType());
		articleDTO.setTitle(article.getTitle());
		articleDTO.setContent(article.getContent());
		articleDTO.setDataTime(article.getDataTime());
		articleDTO.setLeadInTime(article.getLeadInTime().toString());
		articleDTO.setClickNum(article.getClickNum());
		articleDTO.setTenantId(article.getTenantId());
		List<FreeFieldDTO> freeFieldDTOList = new ArrayList<>();
		freeFieldToFreeFieldDTO(article.getFreeFields(),freeFieldDTOList);
		articleDTO.setFreeFields(freeFieldDTOList);
		List<AttachmentDTO> attachmentDTOList = new ArrayList<>();
		attachmentToAttachmentDTO(article.getAttachments(),attachmentDTOList);
		articleDTO.setAttachments(attachmentDTOList);
	}

	public static void freeFieldToFreeFieldDTO(List<FreeField> freeFieldList, List<FreeFieldDTO> freeFieldDTOList) {
		if(freeFieldList != null){
			FreeFieldDTO freeFieldDTO = null;
			for(FreeField freeField : freeFieldList){
				freeFieldDTO = new FreeFieldDTO();
				freeFieldDTO.setName(freeField.getNameCN());
				freeFieldDTO.setContent(freeField.getContent());
				freeFieldDTOList.add(freeFieldDTO);
			}
		}
	}
	public static void attachmentToAttachmentDTO(List<Attachment> attachmentList, List<AttachmentDTO> attachmentDTOList) {
		if(attachmentList != null){
			AttachmentDTO attachmentDTO = null;
			for(Attachment attachment : attachmentList){
				attachmentDTO = new AttachmentDTO();
				attachmentDTO.setFileName(attachment.getFileName());
				attachmentDTO.setFileType(attachment.getFileType());
				attachmentDTO.setFileUrl(attachment.getFileUrl());
				attachmentDTOList.add(attachmentDTO);
			}
		}
	}

}
