/** 
 *  Generated by OpenJPA MetaModel Generator Tool.
**/

package com.x.wcrm.core.entity;

import com.x.base.core.entity.SliceJpaObject_;
import java.lang.Long;
import java.lang.String;
import javax.persistence.metamodel.SingularAttribute;

@javax.persistence.metamodel.StaticMetamodel
(value=com.x.wcrm.core.entity.OperationRecord.class)
@javax.annotation.Generated
(value="org.apache.openjpa.persistence.meta.AnnotationProcessor6",date="Wed Jan 13 10:47:31 CST 2021")
public class OperationRecord_ extends SliceJpaObject_  {
    public static volatile SingularAttribute<OperationRecord,String> content;
    public static volatile SingularAttribute<OperationRecord,String> createuser;
    public static volatile SingularAttribute<OperationRecord,String> crmid;
    public static volatile SingularAttribute<OperationRecord,String> id;
    public static volatile SingularAttribute<OperationRecord,Long> longcreatetime;
    public static volatile SingularAttribute<OperationRecord,Long> longupdatetime;
    public static volatile SingularAttribute<OperationRecord,String> module;
    public static volatile SingularAttribute<OperationRecord,String> operationtype;
}