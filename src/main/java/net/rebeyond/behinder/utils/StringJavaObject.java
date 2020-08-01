//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.rebeyond.behinder.utils;

import java.io.IOException;
import java.net.URI;
import javax.tools.SimpleJavaFileObject;
import javax.tools.JavaFileObject.Kind;

public class StringJavaObject extends SimpleJavaFileObject {
    private String content = "";

    public StringJavaObject(String _javaFileName, String _content) {
        super(_createStringJavaObjectUri(_javaFileName), Kind.SOURCE);
        this.content = _content;
    }

    private static URI _createStringJavaObjectUri(String name) {
        return URI.create("String:///" + name + Kind.SOURCE.extension);
    }

    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return this.content;
    }
}
