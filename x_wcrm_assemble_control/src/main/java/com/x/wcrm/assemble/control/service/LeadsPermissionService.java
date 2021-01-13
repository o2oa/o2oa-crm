package com.x.wcrm.assemble.control.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.entity.annotation.CheckPersistType;
import com.x.base.core.entity.annotation.CheckRemoveType;
import com.x.base.core.project.AbstractContext;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.logger.Logger;
import com.x.base.core.project.logger.LoggerFactory;
import com.x.base.core.project.tools.ListTools;
import com.x.wcrm.assemble.control.Business;
import com.x.wcrm.core.entity.Leads;
import com.x.wcrm.core.entity.Record;
import org.apache.commons.lang3.StringUtils;

public class LeadsPermissionService extends PermissionServiceBase {

	private static Logger logger = LoggerFactory.getLogger(LeadsPermissionService.class);

	// 获得所有下属的线索:列表
	public List<Leads> getList_PersonSubNested(AbstractContext context, Business business,
			EffectivePerson effectivePerson, Integer adjustPage, Integer adjustPageSize, String keyString,
			String orderFieldName, String orderType, Date begintime, Date endtime) throws Exception {
		logger.info("getList_PersonSubNested run!");
		List<String> _me_collection = new ArrayList<String>();
		_me_collection.add(effectivePerson.getDistinguishedName());

		List<String> _persons = new ArrayList<String>();
		_persons = getListWithPersonSubNested(context, effectivePerson, _me_collection); // 所有下属
		// List<Leads> os = business.leadsFactory().ListByOwnerList(_persons);
		List<Leads> os = business.leadsFactory().ListByOwnerList(_persons, adjustPage, adjustPageSize, keyString,
				orderFieldName, orderType, begintime, endtime);
		//校验查询出的线索list是否为空
		if(!os.isEmpty()) {
			//获取跟进记录中的最新跟进记录时间和跟进记录内容，并赋值到线索内容中
			this.getFollowRecords(os, business);
		}
		return os;
	}

	// 获得所有下属的线索:数量
	public long getList_PersonSubNested_count(AbstractContext context, Business business,
			EffectivePerson effectivePerson, Integer adjustPage, Integer adjustPageSize, String keyString,
			String orderFieldName, String orderType, Date begintime, Date endtime) throws Exception {
		logger.info("getList_PersonSubNested run!");
		List<String> _me_collection = new ArrayList<String>();
		_me_collection.add(effectivePerson.getDistinguishedName());

		List<String> _persons = new ArrayList<String>();
		_persons = getListWithPersonSubNested(context, effectivePerson, _me_collection); // 所有下属
		// List<Leads> os = business.leadsFactory().ListByOwnerList(_persons);
		long count = business.leadsFactory().ListByOwnerList_Count(_persons, keyString, begintime, endtime);
		return count;
	}

	// 获得自己负责的线索:列表
	public List<Leads> getList_MyDuty(AbstractContext context, Business business, EffectivePerson effectivePerson,
			Integer adjustPage, Integer adjustPageSize, String keyString, String orderFieldName, String orderType,
			Date begintime, Date endtime) throws Exception {
		List<String> _me = new ArrayList<String>();
		_me.add(effectivePerson.getDistinguishedName());
		// List<Leads> os = business.leadsFactory().ListByOwnerList(_me);
		List<Leads> os = business.leadsFactory().ListByOwnerList(_me, adjustPage, adjustPageSize, keyString,
				orderFieldName, orderType, begintime, endtime);
		//校验查询出的线索list是否为空
		if(!os.isEmpty()) {
			//获取跟进记录中的最新跟进记录时间和跟进记录内容，并赋值到线索内容中
			this.getFollowRecords(os, business);
		}
		return os;
	}

	// 获得自己负责的线索:数量
	public long getList_MyDuty_Count(AbstractContext context, Business business, EffectivePerson effectivePerson,
			String keyString, Date begintime, Date endtime) throws Exception {
		List<String> _me = new ArrayList<String>();
		_me.add(effectivePerson.getDistinguishedName());
		// List<Leads> os = business.leadsFactory().ListByOwnerList(_me);
		long count = business.leadsFactory().ListByOwnerList_Count(_me, keyString, begintime, endtime);
		return count;
	}

	// 获得自己负责的,和我的下属负责线索:列表
	public List<Leads> getList_MyDuty_And_SubNestedDuty(AbstractContext context, Business business,
			EffectivePerson effectivePerson, Integer adjustPage, Integer adjustPageSize, String keyString,
			String orderFieldName, String orderType, Date begintime, Date endtime) throws Exception {
		String _me = effectivePerson.getDistinguishedName();
		List<String> _me_collection = new ArrayList<String>();
		_me_collection.add(effectivePerson.getDistinguishedName());

		List<String> _persons = new ArrayList<String>();
		_persons.add(_me);
		List<String> _subNestedPersons = getListWithPersonSubNested(context, effectivePerson, _me_collection); // 所有下属
		if (ListTools.isNotEmpty(_subNestedPersons)) {
			_persons = ListTools.add(_persons, true, true, _subNestedPersons);
		}
		// List<Leads> os = business.leadsFactory().ListByOwnerList(_persons);
		List<Leads> os = business.leadsFactory().ListByOwnerList(_persons, adjustPage, adjustPageSize, keyString,
				orderFieldName, orderType, begintime, endtime);
		//校验查询出的线索list是否为空
		if(!os.isEmpty()) {
			//获取跟进记录中的最新跟进记录时间和跟进记录内容，并赋值到线索内容中
			this.getFollowRecords(os, business);
		}
		return os;
	}

	// 获得自己负责的,和我的下属负责线索：数量
	public long getList_MyDuty_And_SubNestedDuty_Count(AbstractContext context, Business business,
			EffectivePerson effectivePerson, String keyString, Date begintime, Date endtime) throws Exception {
		String _me = effectivePerson.getDistinguishedName();
		List<String> _me_collection = new ArrayList<String>();
		_me_collection.add(effectivePerson.getDistinguishedName());

		List<String> _persons = new ArrayList<String>();
		_persons.add(_me);
		List<String> _subNestedPersons = getListWithPersonSubNested(context, effectivePerson, _me_collection); // 所有下属
		if (ListTools.isNotEmpty(_subNestedPersons)) {
			_persons = ListTools.add(_persons, true, true, _subNestedPersons);
		}
		// List<Leads> os = business.leadsFactory().ListByOwnerList(_persons);
		long count = business.leadsFactory().ListByOwnerList_Count(_persons, keyString, begintime, endtime);
		return count;
	}

	// 1我自己负责，我下属负责的。2我自己作为团队成员（包括读写权限，和制度权限）的线索
	public List<Leads> getList_MyAll(AbstractContext context, Business business, EffectivePerson effectivePerson)
			throws Exception {
		String _me = effectivePerson.getDistinguishedName();
		List<String> _me_collection = new ArrayList<String>();
		_me_collection.add(effectivePerson.getDistinguishedName());

		List<String> _persons = new ArrayList<String>();
		_persons.add(_me);
		List<String> _subNestedPersons = getListWithPersonSubNested(context, effectivePerson, _me_collection); // 所有下属
		if (ListTools.isNotEmpty(_subNestedPersons)) {
			_persons = ListTools.add(_persons, true, true, _subNestedPersons);
		}

		List<Leads> os = business.leadsFactory().ListByOwnerList_And_TeamMembersReadAndWrite(_me_collection, _me);
		return os;
	}

	// 获得自己负责的,和我的下属负责线索（已转化）:列表
	public List<Leads> getList_MyDuty_And_SubNestedDuty_HasTransform(AbstractContext context, Business business,
			EffectivePerson effectivePerson, Integer adjustPage, Integer adjustPageSize, String keyString,
			String orderFieldName, String orderType) throws Exception {
		String _me = effectivePerson.getDistinguishedName();
		List<String> _me_collection = new ArrayList<String>();
		_me_collection.add(effectivePerson.getDistinguishedName());

		List<String> _persons = new ArrayList<String>();
		_persons.add(_me);
		List<String> _subNestedPersons = getListWithPersonSubNested(context, effectivePerson, _me_collection); // 所有下属
		if (ListTools.isNotEmpty(_subNestedPersons)) {
			_persons = ListTools.add(_persons, true, true, _subNestedPersons);
		}
		// List<Leads> os = business.leadsFactory().ListByOwnerList(_persons);
		List<Leads> os = business.leadsFactory().ListByOwnerList_HasTransform(_persons, adjustPage, adjustPageSize,
				keyString, orderFieldName, orderType);
		//校验查询出的线索list是否为空
		if(!os.isEmpty()) {
			//获取跟进记录中的最新跟进记录时间和跟进记录内容，并赋值到线索内容中
			this.getFollowRecords(os, business);
		}
		return os;
	}

	// 获得自己负责的,和我的下属负责线索(已转化)：数量
	public long getList_MyDuty_And_SubNestedDuty__HasTransform_Count(AbstractContext context, Business business,
			EffectivePerson effectivePerson, String keyString) throws Exception {
		String _me = effectivePerson.getDistinguishedName();
		List<String> _me_collection = new ArrayList<String>();
		_me_collection.add(effectivePerson.getDistinguishedName());

		List<String> _persons = new ArrayList<String>();
		_persons.add(_me);
		List<String> _subNestedPersons = getListWithPersonSubNested(context, effectivePerson, _me_collection); // 所有下属
		if (ListTools.isNotEmpty(_subNestedPersons)) {
			_persons = ListTools.add(_persons, true, true, _subNestedPersons);
		}
		// List<Leads> os = business.leadsFactory().ListByOwnerList(_persons);
		long count = business.leadsFactory().ListByOwnerList_HasTransform_Count(_persons, keyString);
		return count;
	}

    /**
     *  根据线索的ID，获取对应的跟进记录中的最新跟进记录时间和跟进记录内容，并把时间和内容写入到线索内容中返回前端
     * @param osList 线索
     * @param business
     * @throws Exception
     */
    public void getFollowRecords(List<Leads> osList ,Business business) throws Exception {
        for(int i=0;i<osList.size();i++){
            //循环每一条线索
            Leads leadsStr = osList.get(i);

            //System.out.println("输出os11第=="+i+"条ID："+leadsStr.getId());
            //ListByCrmId：根据关联的的crmid列出所有跟进记录，
            List<Record> recordOs = business.recordFactory().ListByCrmId(leadsStr.getId());
            if(!recordOs.isEmpty()){   //跟进记录不为空
                String content = recordOs.get(0).getContent(); //最新跟进记录内容
                Date gjDate = recordOs.get(0).getUpdateTime(); //最新跟进记录更新时间
                //时间、内容 赋值到线索中
                leadsStr.setContent(content);
                leadsStr.setGjTime(gjDate);
            }else{
                leadsStr.setContent("");
                leadsStr.setGjTime(null);
            }
        }
    }

	public void delete( String flag, EffectivePerson currentPerson ) throws Exception {
		if ( StringUtils.isEmpty( flag )) {
			throw new Exception("flag is empty.");
		}
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			Leads lead = emc.find( flag, Leads.class );
			if( lead != null ) {
				emc.beginTransaction( Leads.class );
				emc.remove( lead , CheckRemoveType.all );
				emc.commit();
			}
		} catch (Exception e) {
			throw e;
		}
	}

}