package com.example.ats.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.example.ats.beans.CampaignSearchBean;
import com.example.ats.domain.Campaign;

@Transactional(readOnly=true)
public interface CampaignDAO {
	
	@Transactional(readOnly=false)
	public void save(Campaign campaign);
	
	public Campaign getById(Long id);
	
	@Transactional(readOnly=false)
	public boolean delete(Long id);
	
	public List<Campaign> getBySearchBean(CampaignSearchBean searchBean);
	
	public Long getCountBySearchBean(CampaignSearchBean searchBean);

}
