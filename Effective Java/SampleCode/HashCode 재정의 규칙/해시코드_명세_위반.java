import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Test;

public class 해시코드_명세_위반 {
	public class InterruptionInfo {
		private boolean ndrive = false;
		private int index;
		private Date createDate;
		private String serviceType;
		private String checkType;
		private Date startDate;
		private Date endDate;
		private String message;
		private String apply;
		private String module;
		private int idcNo;

		public boolean isNdrive() {
			return ndrive;
		}

		public void setNdrive(boolean ndrive) {
			this.ndrive = ndrive;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public Date getCreateDate() {
			return createDate;
		}

		public void setCreateDate(Date createDate) {
			this.createDate = createDate;
		}

		public String getServiceType() {
			return serviceType;
		}

		public void setServiceType(String serviceType) {
			this.serviceType = serviceType;
		}

		public String getCheckType() {
			return checkType;
		}

		public void setCheckType(String checkType) {
			this.checkType = checkType;
		}

		public Date getStartDate() {
			return startDate;
		}

		public void setStartDate(Date startDate) {
			this.startDate = startDate;
		}

		public Date getEndDate() {
			return endDate;
		}

		public void setEndDate(Date endDate) {
			this.endDate = endDate;
		}

		public String getApply() {
			return apply;
		}

		/**
		 * @return 적용 여부
		 */
		public boolean isApply() {
			if ("Y".equals(apply)) {
				return true;
			}

			return false;
		}

		public void setApply(String apply) {
			this.apply = apply;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getModule() {
			return module;
		}

		public void setModule(String module) {
			this.module = module;
		}

		public int getIdcNo() {
			return idcNo;
		}

		public void setIdcNo(int idcNo) {
			this.idcNo = idcNo;
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof InterruptionInfo)) {
				return false;
			}

			InterruptionInfo info = (InterruptionInfo) obj;
			return this.index == info.index && StringUtils.equals(this.apply, info.apply) && StringUtils.equals(this.serviceType, info.serviceType) && StringUtils.equals(this.checkType, info.checkType) && equals(this.startDate, info.startDate) && equals(this.endDate, info.endDate);
		}

		@Override
		public int hashCode() {
			return index + createDate.hashCode();
		}

		public int hashCode_HashCodeBuilder() {
			return HashCodeBuilder.reflectionHashCode(this);
		}

		public int hashCode_Arrays() {
			return Arrays.hashCode(new Object[]{this.index, this.createDate, this.serviceType, this.apply, this.checkType, this.endDate, this.idcNo, this.message, this.module, this.ndrive, this.startDate});
			//return Objects.hash(this.index, this.createDate, this.serviceType);
		}

		public int hashCode_Objects() {
			return Objects.hash(this.index, this.createDate, this.serviceType, this.apply, this.checkType, this.endDate, this.idcNo, this.message, this.module, this.ndrive, this.startDate);
		}

		private boolean equals(Date date1, Date date2) {
			if (date1 == date2) {
				return true;
			}

			if (date1 != null && date2 != null && date1.equals(date2)) {
				return true;
			}

			return false;
		}
	}

	@Test
	public void test() {
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);

		InterruptionInfo interruptionInfo_1 = new InterruptionInfo();
		interruptionInfo_1.setIndex(1);
		interruptionInfo_1.setCreateDate(cal.getTime());
		interruptionInfo_1.setServiceType("A");

		InterruptionInfo interruptionInfo_2 = new InterruptionInfo();
		interruptionInfo_2.setIndex(1);
		interruptionInfo_2.setCreateDate(cal.getTime());
		interruptionInfo_2.setServiceType("B");

		System.out.println(interruptionInfo_1.equals(interruptionInfo_2));

		System.out.println(interruptionInfo_1.hashCode());
		System.out.println(interruptionInfo_2.hashCode());

		System.out.println(interruptionInfo_1.hashCode_Arrays());
		System.out.println(interruptionInfo_1.hashCode_HashCodeBuilder());
		System.out.println(interruptionInfo_1.hashCode_Objects());
	}
}
