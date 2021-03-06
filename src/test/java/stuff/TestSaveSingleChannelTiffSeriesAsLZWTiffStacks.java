package stuff;

import de.embl.cba.bdp2.image.Image;
import de.embl.cba.bdp2.open.NamingSchemes;
import de.embl.cba.bdp2.save.SaveFileType;
import de.embl.cba.bdp2.save.SavingSettings;
import de.embl.cba.bdp2.BigDataProcessor2;
import loci.common.DebugTools;

import java.io.File;

import static de.embl.cba.bdp2.utils.FileUtils.createOrEmptyDirectory;
import static junit.framework.TestCase.assertTrue;

public class TestSaveSingleChannelTiffSeriesAsLZWTiffStacks
{
    //@Test
    public void test( )
    {
        DebugTools.setRootLevel("OFF"); // Bio-Formats

        final BigDataProcessor2 bdp = new BigDataProcessor2();

        final String directory =
                "/Users/tischer/Documents/fiji-plugin-bigDataProcessor2/src/test/resources/test-data/nc1-nt3-calibrated-8bit-tiff";

        final String loadingScheme = NamingSchemes.SINGLE_CHANNEL_TIMELAPSE;
        final String filterPattern = ".*.tif";

        final Image image = bdp.openTIFFSeries( directory,  loadingScheme );

//        bdp.showImage( image );
//
        final SavingSettings savingSettings = SavingSettings.getDefaults();
        savingSettings.fileType = SaveFileType.TIFFVolumes;
        savingSettings.numIOThreads = 1;
        savingSettings.numProcessingThreads = 4;
        savingSettings.saveProjections = false;
        savingSettings.saveVolumes = true;
        savingSettings.compression = SavingSettings.COMPRESSION_LZW;
        savingSettings.rowsPerStrip = 1000; // just whole plane

        final String outputDirectory = "/Users/tischer/Documents/fiji-plugin-bigDataProcessor2/src/test/resources/test-output/nc1-nt3-calibrated-8bit-tiff-lzw";

        createOrEmptyDirectory( outputDirectory );

        savingSettings.volumesFilePathStump =
                outputDirectory + "/volume";

        final File testVolumeFile = new File( savingSettings.volumesFilePathStump + "--C00--T00000.ome.tif" );
        if ( testVolumeFile.exists() ) testVolumeFile.delete();

        BigDataProcessor2.saveImageAndWaitUntilDone( image, savingSettings );

        assertTrue( testVolumeFile.exists() );
    }

    public static void main( String[] args )
    {
        new TestSaveSingleChannelTiffSeriesAsLZWTiffStacks().test();
    }

}
