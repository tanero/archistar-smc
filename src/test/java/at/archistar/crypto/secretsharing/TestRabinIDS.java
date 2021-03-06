package at.archistar.crypto.secretsharing;

import at.archistar.crypto.data.Share;
import at.archistar.crypto.decode.DecoderFactory;
import at.archistar.crypto.decode.ErasureDecoderFactory;
import org.junit.Before;

import org.junit.Test;

import java.io.IOException;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Tests for {@link RabinBenOrRSS}
 */
public class TestRabinIDS extends BasicSecretSharingTest {

    public TestRabinIDS() {
        super(8, 3);
    }

    @Before
    public void setup() throws WeakSecurityException {
        DecoderFactory df = new ErasureDecoderFactory();
        algorithm = new RabinIDS(n, k, df);
    }

    @Test
    public void it_produces_shares_of_the_right_size() throws IOException {
        final Share[] shares = algorithm.share(data);
        final int new_length = data.length % k == 0 ? data.length / k : (data.length / k) + 1;
        
        for (Share s : shares) {
            int len1 = s.getYValues().length;
            
            assertThat(len1).isEqualTo(new_length);
        }
    }
}
