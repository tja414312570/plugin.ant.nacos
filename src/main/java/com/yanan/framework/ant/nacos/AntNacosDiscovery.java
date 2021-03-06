package com.yanan.framework.ant.nacos;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.yanan.framework.ant.interfaces.AntDiscoveryService;
import com.yanan.framework.ant.model.AntProvider;
import com.yanan.framework.ant.model.AntProviderSummary;
import com.yanan.framework.ant.model.RegisterResult;
import com.yanan.framework.ant.protocol.ant.interfacer.AntRegisterSatus;
import com.yanan.framework.ant.service.AntRuntimeService;
import com.yanan.framework.plugin.ProxyModel;
import com.yanan.framework.plugin.annotations.Register;

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

	@Override
	public void deregisterService(AntProviderSummary providerSummary) throws Exception {
		nacosRuntime.deregisterInstance(providerSummary);
	}

	@Override
	public AntProviderSummary getService(String name) throws Exception {
		Instance instance = this.nacosRuntime.getInstance(name);
		AntProviderSummary antProviderSummary = new AntProviderSummary();
		antProviderSummary.setHost(instance.getIp());
		antProviderSummary.setAttach(instance);
		antProviderSummary.setName(instance.getClusterName());
		antProviderSummary.setPort(instance.getPort());
		antProviderSummary.setId(instance.getInstanceId());
		return  antProviderSummary;
	}

}
