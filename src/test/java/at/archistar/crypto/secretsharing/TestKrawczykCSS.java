package at.archistar.crypto.secretsharing;

import at.archistar.crypto.data.Share;
import at.archistar.crypto.decode.DecoderFactory;
import at.archistar.crypto.decode.ErasureDecoderFactory;
import at.archistar.crypto.math.GF;
import at.archistar.crypto.math.GFFactory;
import at.archistar.crypto.math.gf256.GF256Factory;
import at.archistar.crypto.random.FakeRandomSource;
import at.archistar.crypto.symmetric.AESEncryptor;
import at.archistar.crypto.symmetric.ChaCha20Encryptor;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Test for {@link KrawczykCSS}
 */
public class TestKrawczykCSS extends BasicSecretSharingTest {

    public TestKrawczykCSS() {
        super(8, 5);
    }

    /* setup and tear-down */
    @Before
    public void setup() throws WeakSecurityException {

        GFFactory gffactory = new GF256Factory();
        DecoderFactory df = new ErasureDecoderFactory(gffactory);
        GF gf = gffactory.createHelper();

        algorithm = new KrawczykCSS(n, k, new FakeRandomSource(), new AESEncryptor(), df, gf);
    }

    @Test
    public void it_produces_shares_of_the_right_size_block_cipher() throws IOException {
        final Share[] shares = algorithm.share(data);
        final int new_data_length = ((data.length / 16) + 1) * 16;
        final int new_length = new_data_length % k == 0 ? new_data_length / k : (new_data_length / k) + 1;
        for (Share s : shares) {
            assertThat(s.getYValues().length).isEqualTo(new_length);
        }
    }

    @Test
    public void it_produces_shares_of_the_right_size_stream_cipher() throws IOException, WeakSecurityException {
        GFFactory gfFactory = new GF256Factory();
        DecoderFactory df = new ErasureDecoderFactory(gfFactory);
        GF gf = gfFactory.createHelper();

        algorithm = new KrawczykCSS(n, k, new FakeRandomSource(), new ChaCha20Encryptor(), df, gf);

        final Share[] shares = algorithm.share(data);
        final int new_length = data.length % k == 0 ? data.length / k : (data.length / k) + 1;
        for (Share s : shares) {
            assertThat(s.getYValues().length).isEqualTo(new_length);
        }
    }
}
