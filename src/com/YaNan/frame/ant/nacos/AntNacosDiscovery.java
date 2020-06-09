package com.YaNan.frame.ant.nacos;

import java.util.ArrayList;
import java.util.List;

import com.YaNan.frame.ant.interfaces.AntDiscoveryService;
import com.YaNan.frame.ant.interfaces.provider.AntRegisterSatus;
import com.YaNan.frame.ant.model.AntProvider;
import com.YaNan.frame.ant.model.AntProviderSummary;
import com.YaNan.frame.ant.model.RegisterResult;
import com.YaNan.frame.ant.service.AntRuntimeService;
import com.YaNan.frame.plugin.ProxyModel;
import com.YaNan.frame.plugin.annotations.Register;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;

@Register(model=ProxyModel.CGLIB)
public class AntNacosDiscovery implements AntDiscoveryService{
	private AntNacosRuntime nacosRuntime;

	public AntNacosRuntime getNacosRuntime() {
		return nacosRuntime;
	}

	public void setNacosRuntime(AntNacosRuntime nacosRuntime) {
		this.nacosRuntime = nacosRuntime;
	}

	@Override
	public List<AntProviderSummary> downloadProviderList(String name) throws NacosException {
		List<Instance> instanceList= this.nacosRuntime.getAllInstances(name);
		List<AntProviderSummary> providerList = new ArrayList<AntProviderSummary>(instanceList.size());
		instanceList.forEach(instance -> {
			AntProviderSummary antProviderSummary = new AntProviderSummary();
			antProviderSummary.setHost(instance.getIp());
			antProviderSummary.setAttach(instance);
			antProviderSummary.setName(instance.getClusterName());
			antProviderSummary.setPort(instance.getPort());
			antProviderSummary.setId(instance.getInstanceId());
			providerList.add(antProviderSummary);
		});
		return providerList;
	}

	@Override
	public void registerService(AntProvider antProvider) throws NacosException {
		RegisterResult  result= new RegisterResult();
		nacosRuntime.register(antProvider);
		result.setStatus(AntRegisterSatus.SUCCESS);
	}

	@Override
	public void avaiable() throws Exception {
		String result = nacosRuntime.getNamaingService().getServerStatus();
		if(!"UP".equals(result))
			throw new RuntimeException("nacos server result :"+result);
	}

	@Override
	public void setAntRuntimeService(AntRuntimeService runtimeService) {
		nacosRuntime.setRuntimeService(runtimeService);
	}

}
