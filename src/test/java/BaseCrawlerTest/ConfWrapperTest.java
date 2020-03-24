package BaseCrawlerTest;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;

import com.ruc.conf.Config;
import com.ruc.conf.ConfigWrapper;

@Slf4j
public class ConfWrapperTest
{
	@Test
	public void testConfLoad(){
		Config config = ConfigWrapper.getInstance().getConfig();
		log.info("the config : {}",config);
	}

}
