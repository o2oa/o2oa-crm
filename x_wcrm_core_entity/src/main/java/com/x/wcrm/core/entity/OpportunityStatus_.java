/** 
 *  Generated by OpenJPA MetaModel Generator Tool.
**/

package com.x.wcrm.core.entity;

import com.x.base.core.entity.SliceJpaObject_;
import java.lang.Integer;
import java.lang.String;
import javax.persistence.metamodel.SingularAttribute;

@javax.persistence.metamodel.StaticMetamodel
(value=com.x.wcrm.core.entity.OpportunityStatus.class)
@javax.annotation.Generated
(value="org.apache.openjpa.persistence.meta.AnnotationProcessor6",date="Wed Jan 13 10:47:31 CST 2021")
public class OpportunityStatus_ extends SliceJpaObject_  {
    public static volatile SingularAttribute<OpportunityStatus,String> id;
    public static volatile SingularAttribute<OpportunityStatus,String> opportunitystatusname;
    public static volatile SingularAttribute<OpportunityStatus,Integer> orderid;
    public static volatile SingularAttribute<OpportunityStatus,String> pinyin;
    public static volatile SingularAttribute<OpportunityStatus,String> pinyinInitial;
    public static volatile SingularAttribute<OpportunityStatus,String> rate;
    public static volatile SingularAttribute<OpportunityStatus,String> typeid;
}
