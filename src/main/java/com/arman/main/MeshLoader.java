package com.arman.main;

import com.arman.geom.Polygon3D;
import com.arman.geom.Vector3D;
import com.arman.models.Mesh;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class MeshLoader {

    private Map<String, LineParser> parsers;
    private Mesh mesh;
    private List<Vector3D> vertices;
    private File path;

    public MeshLoader() {
        this.mesh = null;
        this.vertices = new ArrayList<>();
        this.parsers = new HashMap<>();
        this.parsers.put("obj", new ObjLineParser());
    }

    public Mesh load(String fileName) throws IOException {
        File file = new File(getClass().getClassLoader().getResource(fileName).toExternalForm().substring(6));
        mesh = new Mesh();
        mesh.setFilename(file.getName());
        path = file.getParentFile();
        parseFile(fileName);
        return mesh;
    }

    protected Vector3D getVector(String indexStr) {
        int index = Integer.parseInt(indexStr);
        if (index < 0) {
            index = this.vertices.size() + index + 1;
        }
        return this.vertices.get(index - 1);
    }

    protected void parseFile(String fileName) throws IOException {
        File file = new File(getClass().getClassLoader().getResource(fileName).getPath());
        BufferedReader br = new BufferedReader(new FileReader(file));
        LineParser parser = null;
        int extIndex = fileName.lastIndexOf('.');
        if (extIndex != -1) {
            String ext = fileName.substring(extIndex + 1);
            parser = parsers.get(ext.toLowerCase());
        }
        if (parser == null) {
            parser = parsers.get("obj");
        }
        while (true) {
            String line = br.readLine();
            if (line == null) {
                br.close();
                return;
            }
            line = line.trim();
            if (line.length() > 0 && !line.startsWith("#")) {
                parser.parseLine(line);
            }
        }
    }

    public interface LineParser {

        void parseLine(String line) throws IOException;

    }

    public class ObjLineParser implements LineParser {

        @Override
        public void parseLine(String line) throws IOException {
            StringTokenizer tokenizer = new StringTokenizer(line);
            String command = tokenizer.nextToken();
            if (command.equals("v")) {
                vertices.add(new Vector3D(Float.parseFloat(tokenizer.nextToken()), Float.parseFloat(tokenizer.nextToken()), Float.parseFloat(tokenizer.nextToken())));
            } else if (command.equals("f")) {
                List<Vector3D> faceVertices = new ArrayList<>();
                while (tokenizer.hasMoreTokens()) {
                    String indexStr = tokenizer.nextToken();
                    int endIndex = indexStr.indexOf('/');
                    if (endIndex != -1) {
                        indexStr = indexStr.substring(0, endIndex);
                    }
                    faceVertices.add(getVector(indexStr));
                }
                Vector3D[] arr = new Vector3D[faceVertices.size()];
                faceVertices.toArray(arr);
                Polygon3D poly = new Polygon3D(arr);
                mesh.addFace(poly);
            } else if (command.equals("g")) {
                // TODO: group
            } else if (command.equals("mtllib")) {
                // TODO: materials
            } else if (command.equals("usemtl")) {
                // TODO
            } else {
                // unknown
            }
        }

    }

}
