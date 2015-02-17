import java.util.Date;

public class AirActivity implements java.io.Serializable {

	private Double airActivityId;
	private String airActivityCd;
	private String airActivityDesc;
	private Date updtDt;
	private String updtUsername;
	private Double sid;
	private Double corporateId;

	public Double getAirActivityId() {
		return airActivityId;
	}

	public void setAirActivityId(Double airActivityId) {
		this.airActivityId = airActivityId;
	}

	public String getAirActivityCd() {
		return airActivityCd;
	}

	public void setAirActivityCd(String airActivityCd) {
		this.airActivityCd = airActivityCd;
	}

	public String getAirActivityDesc() {
		return airActivityDesc;
	}

	public void setAirActivityDesc(String airActivityDesc) {
		this.airActivityDesc = airActivityDesc;
	}

	public Date getUpdtDt() {
		return updtDt;
	}

	public void setUpdtDt(Date updtDt) {
		this.updtDt = updtDt;
	}

	public String getUpdtUsername() {
		return updtUsername;
	}

	public void setUpdtUsername(String updtUsername) {
		this.updtUsername = updtUsername;
	}

	public Double getSid() {
		return sid;
	}

	public void setSid(Double sid) {
		this.sid = sid;
	}

	public Double getCorporateId() {
		return corporateId;
	}

	public void setCorporateId(Double corporateId) {
		this.corporateId = corporateId;
	}

}
