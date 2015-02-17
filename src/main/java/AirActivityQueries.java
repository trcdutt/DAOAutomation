public class AirActivityQueries {
	public static final String AIR_ACTIVITY_INSERT = " INSERT INTO AIR_ACTIVITY( AIR_ACTIVITY_CD, AIR_ACTIVITY_DESC, UPDT_DT, UPDT_USERNAME, SID, CORPORATE_ID ) VALUES ( :airActivityCd,:airActivityDesc,:updtDt,:updtUsername,:sid,:corporateId)";

	public static final String AIR_ACTIVITY_UPDATE = " UPDATE AIR_ACTIVITY  set ( AIR_ACTIVITY_CD = :airActivityCd,AIR_ACTIVITY_DESC = :airActivityDesc,UPDT_DT = :updtDt,UPDT_USERNAME = :updtUsername,SID = :sid,CORPORATE_ID = :corporateId)";

	public static final String AIR_ACTIVITY_DELETE = " Delete From AIR_ACTIVITY  Where 1=1 ";

	public static final String AIR_ACTIVITY_SELECT = " Select  AIR_ACTIVITY_CD, AIR_ACTIVITY_DESC, UPDT_DT, UPDT_USERNAME, SID, CORPORATE_ID FROM AIR_ACTIVITY  Where 1=1 ";

}