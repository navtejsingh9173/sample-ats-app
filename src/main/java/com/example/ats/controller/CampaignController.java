package com.example.ats.controller;

import java.util.List;
import java.util.Objects;
import java.util.function.LongUnaryOperator;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ats.beans.CampaignSearchBean;
import com.example.ats.dao.CampaignDAO;
import com.example.ats.domain.Campaign;
import com.example.ats.dto.CampaignCreateDTO;

@RestController
@RequestMapping(path = "/api/v1/campaign")
public class CampaignController {

	@Resource
	private CampaignDAO campaignDAO;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Campaign>> getCampaigns(@RequestParam(required = false) String keyword,
			@RequestParam(required = false) Long id) {
		CampaignSearchBean searchBean = new CampaignSearchBean();

		if (StringUtils.isNotBlank(keyword)) {
			searchBean.setKeyword(keyword);
		}
		searchBean.setId(Objects.isNull(id) && NumberUtils.isDigits(keyword) ? Long.valueOf(keyword) : id);
		return new ResponseEntity<>(campaignDAO.getBySearchBean(searchBean), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> createCampaign(@Valid @RequestBody CampaignCreateDTO campaignCreateDTO) {
		Campaign campaign;
		CampaignSearchBean searchBean = new CampaignSearchBean();
		searchBean.setTitle(campaignCreateDTO.getTitle());
		Long existingCount = campaignDAO.getCountBySearchBean(searchBean);
		if (!existingCount.equals(0L) && Objects.isNull(campaignCreateDTO.getId())) {
			throw new IllegalArgumentException("Campaign with the same title exists.");
		} else if (Objects.nonNull(campaignCreateDTO.getId())) {
			campaign = campaignDAO.getById(campaignCreateDTO.getId());
			if (Objects.isNull(campaign)) {
				throw new IllegalArgumentException("Invalid campaign ID.");
			}
		} else {
			campaign = new Campaign();
		}

		campaign.setDescription(campaignCreateDTO.getDescription());
		campaign.setTitle(campaignCreateDTO.getTitle());
		campaign.setCategory(campaignCreateDTO.getCategory().name());

		campaignDAO.save(campaign);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteCampaign(@RequestParam Long id) {
		CampaignSearchBean searchBean = new CampaignSearchBean();
		searchBean.setId(id);
		if (campaignDAO.getCountBySearchBean(searchBean).equals(0L)) {
			throw new IllegalArgumentException("Campaign with the requested ID does not exists.");
		}

		return new ResponseEntity<>(campaignDAO.delete(id), HttpStatus.OK);
	}

}
