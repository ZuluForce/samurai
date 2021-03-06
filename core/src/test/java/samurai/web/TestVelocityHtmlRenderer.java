/*
 * Copyright 2003-2012 Yusuke Yamamoto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package samurai.web;

import junit.framework.TestCase;
import samurai.core.ThreadDumpExtractor;
import samurai.core.ThreadStatistic;

import java.io.File;
import java.io.IOException;

public class TestVelocityHtmlRenderer extends TestCase {
    ThreadStatistic statistic = new samurai.core.ThreadStatistic();

    public TestVelocityHtmlRenderer(String name) {
        super(name);
    }

    public static void main(String[] args) throws IOException {
        //do a performance test
        analyze("BEA", "testcases/BEA/910JRockit.dmp", 100);
        analyze("IBM", "testcases/IBM/1.4.2IBM/javacore.20060511.172914.516.txt", 200);
        analyze("SUN", "testcases/Sun/sun1.4.2_03stacked.dmp", 1000);
    }

    private static void analyze(String vendor, String fileName, int count) throws IOException {
        ThreadStatistic stats = new samurai.core.ThreadStatistic();
        ThreadDumpExtractor analyzer = new ThreadDumpExtractor(stats);
        analyzer.analyze(new File(fileName));
        VelocityHtmlRenderer renderer = new VelocityHtmlRenderer("samurai/web/outcss.vm");
        //warm up
        System.out.println("Warming up " + vendor + "...");
        for (int i = 0; i < 100; i++) {
            renderer.saveTo(stats, null, new ProgressListener() {
                public void notifyProgress(int finished, int all) {
                }
            });
        }
        System.gc();
        System.out.println("Running " + vendor + "...");
        long before = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            renderer.saveTo(stats, null, new ProgressListener() {
                public void notifyProgress(int finished, int all) {
                }
            });
        }
        long timeSpent = System.currentTimeMillis() - before;
        System.out.println("Time spent " + vendor + ":" + (timeSpent / 1000d) + " secs");
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }


    public void testEscape() throws IOException {
        VelocityHtmlRenderer.Util util = new VelocityHtmlRenderer.Util();
        assertEquals("foo&lt;bar",util.escape("foo<bar"));
        assertEquals("foo&gt;bar",util.escape("foo>bar"));
        assertEquals("foo&lt;&gt;bar",util.escape("foo<>bar"));
        assertEquals("&lt;foo&lt;&lt;foo&gt;&gt;bar&gt;",util.escape("<foo<<foo>>bar>"));
    }
    public void testSaveTo() throws IOException {
        ThreadDumpExtractor analyzer = new ThreadDumpExtractor(statistic);
        analyzer.analyze(new File("testcases/Sun/1.4.2_03Sunstacked.dmp"));
        VelocityHtmlRenderer renderer = new VelocityHtmlRenderer("samurai/web/outcss.vm");
        renderer.saveTo(statistic, new File("savedhtml"), new ProgressListener() {
            public void notifyProgress(int finished, int all) {
                assertTrue(finished <= all);
            }
        });
    }

}
