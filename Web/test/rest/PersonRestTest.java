package rest;

import junit.framework.TestCase;

public class PersonRestTest extends TestCase {
/*	
	RestCaller<Party> pc = new RestCaller<Party>(RestConstant.REST_SERVICE, RestServiceConstant.PARTY);
	RestCaller<AppRole> roc = new RestCaller<AppRole>(RestConstant.REST_SERVICE, RestServiceConstant.ROLE_SERVICE);
	RestCaller<Person> rc = new RestCaller<Person>(RestConstant.REST_SERVICE, RestServiceConstant.PERSON);
	RestCaller<Company> cc = new RestCaller<Company>(RestConstant.REST_SERVICE, RestServiceConstant.COMPANY);
	RestCaller<OrganisationUnit> oc = new RestCaller<OrganisationUnit>(RestConstant.REST_SERVICE, RestServiceConstant.ORG_UNIT);
	
	private Person createPerson(String name){
		Person anObject = new Person();
		anObject.setActive(true);
		anObject.setAddress("Jl. Jalan Gg. Gang No. Nomor");
		anObject.setBirthPlace("Tempat Lahir");
		anObject.setCity("Kota masa kini");
		anObject.setDateOfBirth(DateTimeFunction.addDate(new Date(), -10, Calendar.YEAR));
		anObject.setEmail("alamat@internet.com");
		anObject.setHpNumber("08123456789");
		anObject.setIdNumber("ID1234567");
		anObject.setName(name);
		anObject.setPhoneNumber("0211234567");
		anObject.setProvince("Provinsi");
		anObject.setRtNumber("001");
		anObject.setRwNumber("002");
//		anObject.setSex(1);
		anObject.setSubdistrict("Sub District");
		anObject.setVillage("Desa Sukamaju");
		anObject.setZipCode("174111");
		return anObject;
	}
	
	private AppUser createUser(String username){
		AppUser user = new AppUser();
		user.setUserName(username);
		user.setPassword("master");
		user.setDefaultPassword(true);
		user.setUserType(SystemConstant.USER_TYPE_INTERNAL);
		user.setLock(false);
		user.setState(0);
		user.setFailedLoginCounter(0);
		user.setMustChangePassword(false);
		List<SearchFilter> roleFilter = new ArrayList<SearchFilter>();
		roleFilter.add(new SearchFilter(AppRole.NAME, Operator.EQUALS, "AAL"));
		List<AppRole> appRoles = roc.findAll(roleFilter, null);
		user.setAppRoles(appRoles);
		return user;
	}
	
	@Test
	public void testPerson1() throws Exception {
		//test list get size	
		List<SearchFilter> filter = new ArrayList<SearchFilter>();
		List<SearchOrder> order = new ArrayList<SearchOrder>();
		PagingWrapper<Person> pagings = rc.findAllByFilter(1, 10, filter, order);
		assertNotNull(pagings.getResult());
		int size = pagings.getResult().size();
		int random = RandomUtils.nextInt();
		String name = "Depan Tengah Belakang"+random+DateTimeFunction.date2String(new Date(), SystemConstant.SYSTEM_REPORT_DATE);
		//test create person only
		Person anObject = createPerson(name);
		
		rc.saveOrUpdate(anObject);
		
		//test record added
		PagingWrapper<Person> pagings2 = rc.findAllByFilter(1, 10, filter, order);
		assertNotNull(pagings2.getResult());
		int newSize = pagings2.getResult().size();
		assertTrue(newSize<10?newSize==size+1:true);
		
		List<Long> objectPKs = new ArrayList<Long>();
		int i = newSize>1?newSize-1:newSize;
		System.out.println("i size= "+i);
		int j = 1;
		for (Person human : pagings2.getResult()) {
			objectPKs.add(human.getPkParty());
			j++;
			if(j>i){
				break;
			}
		}
		System.out.println("objectPKs.size()="+objectPKs.size());
		
		List<Person> people = rc.findObjects(objectPKs.toArray(new Long[objectPKs.size()]));
		System.out.println("people.size()="+people.size());
		assertTrue(people.size()==i);
		
		//test find the person
		filter.add(new SearchFilter("name", Operator.EQUALS, name));
		List<Person> persons = rc.findAll(filter, order);
		assertTrue(persons.size()==1);
		
		//test update person
		Person newPerson = persons.get(0);
		newPerson.setActive(false);
		newPerson.setBirthPlace("Birthplace");
//		newPerson.setSex(2);
		newPerson.setDateOfBirth(new Date());
		
		rc.saveOrUpdate(newPerson);
		
		//test cek hasil update
		Person samePerson = rc.findById(newPerson.getPkParty());
		assertNotNull(samePerson);
		assertEquals(newPerson.getActive(), samePerson.getActive());
		assertEquals(newPerson.getBirthPlace(), samePerson.getBirthPlace());
		assertEquals(newPerson.getSex(), samePerson.getSex());
		assertEquals(newPerson.getDateOfBirth(), samePerson.getDateOfBirth());
		assertEquals(newPerson.getName(), samePerson.getName());
		
		//test delete person
		rc.delete(new Long[]{samePerson.getPkParty()});
		
		Person deletedPerson = rc.findById(samePerson.getPkParty());
		assertNull(deletedPerson);
	}
	
	@Test
	public void testPerson2() throws Exception {
		//test list get size	
		List<SearchFilter> filter = new ArrayList<SearchFilter>();
		List<SearchOrder> order = new ArrayList<SearchOrder>();
		PagingWrapper<Person> pagings = rc.findAllByFilter(1, 10, filter, order);
		assertNotNull(pagings.getResult());
		int size = pagings.getResult().size();
		int random = RandomUtils.nextInt();
		String name = "Depan Tengah Belakang"+random+DateTimeFunction.date2String(new Date(), SystemConstant.SYSTEM_REPORT_DATE);
		//test create person only
		Person anObject = createPerson(name);
		
		Set<Partner> partyRoles = new HashSet<Partner>();
		Partner partyRole = new Partner();
		Partner agent = new Partner();
		partyRole.setParty(anObject);
		partyRoles.add(partyRole);
		anObject.setPartners(partyRoles);
		rc.saveOrUpdate(anObject);
		
		//test record added
		PagingWrapper<Person> pagings2 = rc.findAllByFilter(1, 10, filter, order);
		assertNotNull(pagings2.getResult());
		int newSize = pagings2.getResult().size();
		assertTrue(newSize<10?newSize==size+1:true);
		
		List<Long> objectPKs = new ArrayList<Long>();
		int i = newSize>1?newSize-1:newSize;
		System.out.println("i size= "+i);
		int j = 1;
		for (Person human : pagings2.getResult()) {
			objectPKs.add(human.getPkParty());
			j++;
			if(j>i){
				break;
			}
		}
		System.out.println("objectPKs.size()="+objectPKs.size());
		
		List<Person> people = rc.findObjects(objectPKs.toArray(new Long[objectPKs.size()]));
		System.out.println("people.size()="+people.size());
		assertTrue(people.size()==i);
		
		//test find the person
		filter.add(new SearchFilter("name", Operator.EQUALS, name));
		List<Person> persons = rc.findAll(filter, order);
		assertTrue(persons.size()==1);
		
		//test update person
		Person newPerson = persons.get(0);
		Set<Partner> check1 = newPerson.getPartners();
		assertNotNull(check1);
		assertTrue(check1.size()>0);
		PartyRole check2 = check1.iterator().next();
		assertNotNull(check2);
		newPerson.setActive(false);
		newPerson.setBirthPlace("Birthplace");
//		newPerson.setSex(2);
		newPerson.setDateOfBirth(new Date());
		
		rc.saveOrUpdate(newPerson);
		
		//test cek hasil update
		Person samePerson = rc.findById(newPerson.getPkParty());
		assertNotNull(samePerson);
		assertEquals(newPerson.getActive(), samePerson.getActive());
		assertEquals(newPerson.getBirthPlace(), samePerson.getBirthPlace());
		assertEquals(newPerson.getSex(), samePerson.getSex());
		assertEquals(newPerson.getDateOfBirth(), samePerson.getDateOfBirth());
		assertEquals(newPerson.getName(), samePerson.getName());
		
		//test delete person
		rc.delete(new Long[]{samePerson.getPkParty()});
		
		Person deletedPerson = rc.findById(samePerson.getPkParty());
		assertNull(deletedPerson);
	}
	
	public void testUserWithoutAppUser() throws Exception{
		//test list get size	
		List<SearchFilter> filter = new ArrayList<SearchFilter>();
		List<SearchOrder> order = new ArrayList<SearchOrder>();
		PagingWrapper<Person> pagings = rc.findAllByFilter(1, 10, filter, order);
		assertNotNull(pagings.getResult());
		int random = RandomUtils.nextInt();
		String supervisorName = "Super"+random+DateTimeFunction.date2String(new Date(), SystemConstant.SYSTEM_REPORT_DATE);
		String name = "Depan Tengah Belakang"+random+DateTimeFunction.date2String(new Date(), SystemConstant.SYSTEM_REPORT_DATE);
		//test create person only
		filter.add(new SearchFilter(Company.NAME, Operator.LIKE, "AAL"));
		List<Company> companies = cc.findAll(filter, order);
		assertNotNull(companies);
		assertNotNull(companies.get(0));

		Person supervisor = createPerson(supervisorName);
		Set<Employment> semployments = new HashSet<Employment>();
		Set<JobFunction> sjobFunctions = new HashSet<JobFunction>();
		JobFunction supervisorJob = new JobFunction();
		Employment sworkAt = new Employment();
		sworkAt.setCompany(companies.get(0));
		supervisorJob.setEmployment(sworkAt);
		sworkAt.setPerson(supervisor);
		sworkAt.setJobFunctions(sjobFunctions);
		sjobFunctions.add(supervisorJob);
		semployments.add(sworkAt);
		supervisor.setEmployments(semployments);
		
		Lookup role = new Lookup();
		role.setPkLookup(4L);
		
		Set<Partner> spartyRoles = new HashSet<Partner>();
		Partner sinternalUserRole = new Partner();
		sinternalUserRole.setRole(role);
		sinternalUserRole.setParty(supervisor);
		spartyRoles.add(sinternalUserRole);
		supervisor.setPartners(spartyRoles);
		
		rc.saveOrUpdate(supervisor);
		
		//test find the person
		filter.clear();
		order.clear();
		filter.add(new SearchFilter(Person.NAME, Operator.EQUALS, supervisorName));
		List<Person> supervisors = rc.findAll(filter, order);
		assertTrue(supervisors.size()==1);
		Person theSup = supervisors.get(0);
		Iterator eterator = theSup.getEmployments().iterator();
		Employment supEmployment = (Employment) eterator.next();
		Iterator supjfiterator = supEmployment.getJobFunctions().iterator();
		Person anObject = createPerson(name);
		
		Set<Employment> employments = new HashSet<Employment>();
		Set<JobFunction> jobFunctions = new HashSet<JobFunction>();
		JobFunction employee = new JobFunction();
		Employment workAt = new Employment();
		filter.clear();
		workAt.setCompany(companies.get(0));
		workAt.setPerson(anObject);
		workAt.setJobFunctions(jobFunctions);
		employee.setEmployment(workAt);
		employee.setParentJobFunction((JobFunction) supjfiterator.next());
		jobFunctions.add(employee);
		employments.add(workAt);
		anObject.setEmployments(employments);
		
		Set<Partner> partyRoles = new HashSet<Partner>();
		Partner internalUserRole = new Partner();
		internalUserRole.setRole(role);
		internalUserRole.setParty(supervisor);
		partyRoles.add(internalUserRole);
		supervisor.setPartners(partyRoles);
		
		rc.saveOrUpdate(anObject);
		
		//test find the person
		filter.clear();
		order.clear();
		filter.add(new SearchFilter(Person.NAME, Operator.EQUALS, name));
		List<Person> persons = rc.findAll(filter, order);
		assertTrue(persons.size()==1);
		
		Person newPerson = persons.get(0);
		
		//test delete person
		rc.delete(new Long[]{newPerson.getPkParty()});
		
		Person deletedPerson = rc.findById(newPerson.getPkParty());
		assertNull(deletedPerson);
		
		//error null delete
		rc.delete(new Long[]{theSup.getPkParty()});
		
		Person deletedSup = rc.findById(theSup.getPkParty());
		assertNull(deletedSup);
	}
	
	public void testUserWithoutAppUserButWithManagementUnit() throws Exception{
		//test list get size	
		List<SearchFilter> filter = new ArrayList<SearchFilter>();
		List<SearchOrder> order = new ArrayList<SearchOrder>();
		PagingWrapper<Person> pagings = rc.findAllByFilter(1, 10, filter, order);
		assertNotNull(pagings.getResult());
		int random = RandomUtils.nextInt();
		String supervisorName = "Super"+random+DateTimeFunction.date2String(new Date(), SystemConstant.SYSTEM_REPORT_DATE);
		String name = "Depan Tengah Belakang"+random+DateTimeFunction.date2String(new Date(), SystemConstant.SYSTEM_REPORT_DATE);
		//test create person only
		filter.add(new SearchFilter(Company.NAME, Operator.LIKE, "ADF1310800881"));
//		filter.add(new SearchFilter(Company.NAME, Operator.LIKE, "AAL"));
		List<Company> companies = cc.findAll(filter, order);
		assertNotNull(companies);
		Company cmp = companies.get(0);
		assertNotNull(cmp);

		Person supervisor = createPerson(supervisorName);
		Set<Employment> semployments = new HashSet<Employment>();
		Set<JobFunction> sjobFunctions = new HashSet<JobFunction>();
		JobFunction supervisorJob = new JobFunction();
		Employment sworkAt = new Employment();
		sworkAt.setCompany(cmp);
		supervisorJob.setEmployment(sworkAt);
		ManagementUnit mgmtUnit = cmp.getManagementUnits().iterator().next();
		supervisorJob.setOrganisationUnit(mgmtUnit);
		sworkAt.setPerson(supervisor);
		sworkAt.setJobFunctions(sjobFunctions);
		sjobFunctions.add(supervisorJob);
		semployments.add(sworkAt);
		supervisor.setEmployments(semployments);
		
		Lookup role = new Lookup();
		role.setPkLookup(4L);
		
		Set<Partner> spartyRoles = new HashSet<Partner>();
		Partner sinternalUserRole = new Partner();
		sinternalUserRole.setRole(role);
		sinternalUserRole.setParty(supervisor);
		spartyRoles.add(sinternalUserRole);
		supervisor.setPartners(spartyRoles);
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.registerModule(new Hibernate4Module());
		String supjson = mapper.writeValueAsString(supervisor);
		System.out.println(supjson);
		
		rc.saveOrUpdate(supervisor);
		
		//test find the person
		filter.clear();
		order.clear();
		filter.add(new SearchFilter(Person.NAME, Operator.EQUALS, supervisorName));
		List<Person> supervisors = rc.findAll(filter, order);
		assertTrue(supervisors.size()==1);
		Person theSup = supervisors.get(0);
		Iterator eterator = theSup.getEmployments().iterator();
		Employment supEmployment = (Employment) eterator.next();
		Iterator supjfiterator = supEmployment.getJobFunctions().iterator();
		Person anObject = createPerson(name);
		
		Set<Employment> employments = new HashSet<Employment>();
		Set<JobFunction> jobFunctions = new HashSet<JobFunction>();
		JobFunction employee = new JobFunction();
		Employment workAt = new Employment();
		workAt.setCompany(cmp);
		employee.setEmployment(workAt);
		employee.setOrganisationUnit(mgmtUnit);
		employee.setParentJobFunction((JobFunction) supjfiterator.next());
		workAt.setPerson(anObject);
		workAt.setJobFunctions(jobFunctions);
		jobFunctions.add(employee);
		employments.add(workAt);
		anObject.setEmployments(employments);
		
		Set<Partner> partyRoles = new HashSet<Partner>();
		Partner internalUserRole = new Partner();
		internalUserRole.setRole(role);
		internalUserRole.setParty(supervisor);
		partyRoles.add(internalUserRole);
		supervisor.setPartners(partyRoles);
		String empjson = mapper.writeValueAsString(anObject);
		System.out.println(empjson);
		Person emp = mapper.readValue(empjson, new TypeReference<Person>(){});
		rc.saveOrUpdate(anObject);
		
		//test find the person
		filter.clear();
		order.clear();
		filter.add(new SearchFilter(Person.NAME, Operator.EQUALS, name));
		List<Person> persons = rc.findAll(filter, order);
		assertTrue(persons.size()==1);
		
		Person newPerson = persons.get(0);
		
		//test delete person
		rc.delete(new Long[]{newPerson.getPkParty()});
		
		Person deletedPerson = rc.findById(newPerson.getPkParty());
		assertNull(deletedPerson);
		
		//error null delete
		rc.delete(new Long[]{theSup.getPkParty()});
		
		Person deletedSup = rc.findById(theSup.getPkParty());
		assertNull(deletedSup);
	}
	
	public void testUserWithAppUser() throws Exception{
		//test list get size	
		List<SearchFilter> filter = new ArrayList<SearchFilter>();
		List<SearchOrder> order = new ArrayList<SearchOrder>();
		PagingWrapper<Person> pagings = rc.findAllByFilter(1, 10, filter, order);
		assertNotNull(pagings.getResult());
		int random = RandomUtils.nextInt();
		String supervisorName = "Super"+random+DateTimeFunction.date2String(new Date(), SystemConstant.SYSTEM_REPORT_DATE);
		String name = "Depan Tengah Belakang"+random+DateTimeFunction.date2String(new Date(), SystemConstant.SYSTEM_REPORT_DATE);
		//test create person only
		filter.add(new SearchFilter(Company.NAME, Operator.LIKE, "AAL"));
		List<Company> companies = cc.findAll(filter, order);
		assertNotNull(companies);
		assertNotNull(companies.get(0));
	
		Person supervisor = createPerson(supervisorName);
		Set<Employment> semployments = new HashSet<Employment>();
		Set<JobFunction> sjobFunctions = new HashSet<JobFunction>();
		JobFunction supervisorJob = new JobFunction();
		Employment sworkAt = new Employment();
		sworkAt.setCompany(companies.get(0));
		supervisorJob.setEmployment(sworkAt);
		sworkAt.setPerson(supervisor);
		sworkAt.setJobFunctions(sjobFunctions);
		sjobFunctions.add(supervisorJob);
		semployments.add(sworkAt);
		supervisor.setEmployments(semployments);
		
		Lookup role = new Lookup();
		role.setPkLookup(4L);
		
		Set<Partner> spartyRoles = new HashSet<Partner>();
		Partner sinternalUserRole = new Partner();
		sinternalUserRole.setRole(role);
		sinternalUserRole.setParty(supervisor);
		spartyRoles.add(sinternalUserRole);
		supervisor.setPartners(spartyRoles);
		
//		rc.saveOrUpdate(supervisor);
		Map<String,Object> multipleObjects = new HashMap<String, Object>();
		multipleObjects.put("person", supervisor);
		
		String supusername = "TPERSON"+random+Calendar.getInstance().get(Calendar.DATE);
		AppUser user = createUser(supusername);
		
		multipleObjects.put("user", user);
		pc.saveOrUpdate(multipleObjects, "/createPersonUser");
		
		//test find the person
		filter.clear();
		order.clear();
		filter.add(new SearchFilter(Person.NAME, Operator.EQUALS, supervisorName));
		List<Person> supervisors = rc.findAll(filter, order);
		assertTrue(supervisors.size()==1);
		Person theSup = supervisors.get(0);
		Iterator eterator = theSup.getEmployments().iterator();
		Employment supEmployment = (Employment) eterator.next();
		Iterator supjfiterator = supEmployment.getJobFunctions().iterator();
		Person anObject = createPerson(name);
		
		Set<Employment> employments = new HashSet<Employment>();
		Set<JobFunction> jobFunctions = new HashSet<JobFunction>();
		JobFunction employee = new JobFunction();
		Employment workAt = new Employment();
		filter.clear();
		workAt.setCompany(companies.get(0));
		workAt.setPerson(anObject);
		workAt.setJobFunctions(jobFunctions);
		employee.setEmployment(workAt);
		employee.setParentJobFunction((JobFunction) supjfiterator.next());
		jobFunctions.add(employee);
		employments.add(workAt);
		anObject.setEmployments(employments);
		
		Set<Partner> partyRoles = new HashSet<Partner>();
		Partner internalUserRole = new Partner();
		internalUserRole.setRole(role);
		internalUserRole.setParty(supervisor);
		partyRoles.add(internalUserRole);
		supervisor.setPartners(partyRoles);
		
		String username = "TPERSON"+random+Calendar.getInstance().get(Calendar.DATE);
		AppUser employeeUser = createUser(username);
		
		multipleObjects.clear();
		multipleObjects.put("person", anObject);
		multipleObjects.put("user", employeeUser);
		
		pc.saveOrUpdate(multipleObjects, "/createPersonUser");
		
		//test find the person
		filter.clear();
		order.clear();
		filter.add(new SearchFilter(Person.NAME, Operator.EQUALS, name));
		List<Person> persons = rc.findAll(filter, order);
		assertTrue(persons.size()==1);
		
		Person newPerson = persons.get(0);
		
		//test delete person
		rc.delete(new Long[]{newPerson.getPkParty()});
		
		Person deletedPerson = rc.findById(newPerson.getPkParty());
		assertNull(deletedPerson);
		
		//error null delete
		rc.delete(new Long[]{theSup.getPkParty()});
		
		Person deletedSup = rc.findById(theSup.getPkParty());
		assertNull(deletedSup);
	}

*/}
