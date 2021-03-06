package develop;

import de.embl.cba.bdp2.open.fileseries.FileInfos;
import de.embl.cba.bdp2.open.NamingSchemes;
import ij.IJ;
import ij.ImageJ;
import ij.io.FileInfo;
import ij.io.TiffDecoder;

import java.io.File;
import java.io.IOException;

public class ImageCalibrationTiffReaderTest
{
	public static void main( String[] args ) throws IOException
	{
		new ImageJ();

		final File file = new File(
				ImageCalibrationTiffReaderTest.class.getResource( "nc1-nt1-calibrated-tiff" ).getFile() );

		IJ.openImage( file.toString() + "/mri-stack.tif" ).show();

		final TiffDecoder tiffDecoder = new TiffDecoder(
				file.toString(), "mri-stack.tif" );
		final FileInfo[] tiffInfo = tiffDecoder.getTiffInfo();
	}
}
