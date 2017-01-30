package id.base.app.rest;

public class RestConstant {
	public static final String USER_CALLER = "uLUoI0BMv1";
	public static String REST_SERVICE = "http://localhost:8181/ApplicationService";
	
	public static void setRestService(String url){
		REST_SERVICE = url;
	}
	
	public static final String RM_DASHBOARD = "/dashboard";
	
	public static final String RM_USER = "/user";
	public static final String RM_ROLE = "/role";
	
	public static final String RM_LOOKUP = "/lookup";
	public static final String RM_LOOKUP_GROUP = "/lookupGroup";
	public static final String RM_LOOKUP_ADDRESS = "/lookupAddress";
	public static final String RM_LOOKUP_ADDRESS_GROUP = "/lookupAddressGroup";
	public static final String RM_MASTER_ADDRESS = "/masterAddress";
	public static final String RM_MASTER_FEE = "/masterFee";
	public static final String RM_ERROR = "/error";
	public static final String RM_SYSTEM_PARAMETER = "/systemParameter";
	public static final String RM_EMAIL_TEMPLATE = "/emailTemplate";
	public static final String RM_FAQ = "/faq";
	public static final String RM_HELPER = "/helper";
	
	public static final String RM_SLIDESHOW = "/slideshow";
	public static final String RM_TEAM = "/team";
	public static final String RM_ABOUT = "/about";
	public static final String RM_CONTACT = "/contact";
	
	public static final String RM_REPORT = "/report";
	
	public static final String RM_USER_LOGIN = "/userLogin";
	public static final String RM_AUTHENTICATION = "/auth";
	public static final String RM_MAIL = "/mailController";
	
	public static final String RM_ROLE_FUNCTION = "/roleFunction";
	public static final String RM_APP_FUNCTION = "/appFunction";
	public static final String RM_AUDIT = "/audit";
	public static final String RM_SECURITYPARAM = "/securityParam";
	
	public static final String RM_PARTY = "/party";
	public static final String RM_PARTY_ROLE = "/partyRole";
	public static final String RM_PARTY_ADDRESS = "/partyAddress";
	public static final String RM_PARTY_ID = "/partyID";
	public static final String RM_PARTY_CONTACT = "/partyContact";
	
	//master
	public static final String RM_COMPANY = "/company";
	public static final String RM_COMPANY_LOOKUP = "/companyLookup";
	public static final String RM_COMPANY_PARTY = "/companyParty";
	public static final String RM_COMPANY_PRODUCT = "/product";
	public static final String RM_COMPANY_WAREHOUSE = "/companyWarehouse";
	public static final String RM_COMPANY_MACHINERY = "/companyMachinery";
	public static final String RM_COMPANY_TRANSPORTER = "/companyTransporter";
	public static final String RM_COMPANY_THIRD_PARTY = "/companyThirdParty";
	public static final String RM_COMPANY_MASTER_FEE = "/companyMasterFee";
	public static final String RM_STOCK = "/stock";
	
	//initial wizard master
	public static final String RM_INITIAL_WIZARD = "/initialWizard";
	
	//procurement
	public static final String RM_PURCHASE_ORDER = "/purchaseOrder";
	public static final String RM_TRANS_IN = "/transIn";
	public static final String RM_TRANS_IN_ITEM = "/transInItem";

	//inventory
	public static final String RM_DISPATCH_ORDER_NOTE = "/pengirimanBarang";
	public static final String RM_GOODS_RECEIPT_NOTE = "/penerimaanBarang";
	
	//production
	public static final String RM_TRANS_PROD = "/transProd";
	
	//sales
	public static final String RM_TRANS_OUT = "/transOut";
	
	//report
	public static final String RM_REPORT_COST_EXPENSES = "/costExpensesReport";
	public static final String RM_REPORT_STOCK = "/reportStock";
	public static final String RM_REPORT_TRANS_IN = "/transInReport";
	public static final String RM_REPORT_CASH_FLOW = "/cashFlowReport";
	public static final String RM_REPORT_TRANS_OUT = "/transOutReport";
	
	//business report
	public static final String RM_CASH_FLOW = "/cashFlow";
	
	//forecast call
	public static final String RM_FORECAST_CALL = "/forecastCall";
}
