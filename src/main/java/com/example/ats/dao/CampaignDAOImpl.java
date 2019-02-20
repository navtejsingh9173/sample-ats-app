package com.example.ats.dao;

import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.example.ats.beans.CampaignSearchBean;
import com.example.ats.domain.Campaign;

@Repository
public class CampaignDAOImpl implements CampaignDAO {

	@Resource
	private SessionFactory sessionFactory;

	private Class<Campaign> getDataClass() {
		return Campaign.class;
	}

	private Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	private Criteria getCriteria() {
		return getSession().createCriteria(getDataClass());
	}

	@Override
	public void save(Campaign campaign) {
		getSession().saveOrUpdate(campaign);
	}
	
	@Override
	public Campaign getById(Long id) {
		return getSession().get(Campaign.class, id);
	}

	@Override
	public boolean delete(Long id) {
		Session session = getSession();
		Campaign campaign = (Campaign) session.load(getDataClass(), id);
		if (Objects.nonNull(campaign)) {
			session.delete(campaign);
			return true;
		}
		return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Campaign> getBySearchBean(CampaignSearchBean searchBean) {
		Criteria criteria = getCriteriaBySearchBean(searchBean);
		return criteria.list();
	}

	private Criteria getCriteriaBySearchBean(CampaignSearchBean searchBean) {
		Criteria criteria = getCriteria();

		if (Objects.isNull(searchBean)) {
			return criteria;
		}

		if (StringUtils.isNotEmpty(searchBean.getKeyword())) {
			Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(Restrictions.ilike("description", searchBean.getKeyword(), MatchMode.ANYWHERE));
			disjunction.add(Restrictions.ilike("title", searchBean.getKeyword(), MatchMode.ANYWHERE));
			disjunction.add(Restrictions.ilike("category", searchBean.getKeyword(), MatchMode.ANYWHERE));
			if (Objects.nonNull(searchBean.getId())) {
				disjunction.add(Restrictions.eq("id", searchBean.getId()));
			}
			criteria.add(disjunction);
		} else if (Objects.nonNull(searchBean.getId())) {
			criteria.add(Restrictions.eq("id", searchBean.getId()));
		}

		if (StringUtils.isNotEmpty(searchBean.getTitle())) {
			criteria.add(Restrictions.ilike("title", searchBean.getTitle(), MatchMode.ANYWHERE));
		}

		return criteria;
	}

	@Override
	public Long getCountBySearchBean(CampaignSearchBean searchBean) {
		return (Long) getCriteriaBySearchBean(searchBean).setProjection(Projections.rowCount()).uniqueResult();
	}

}
