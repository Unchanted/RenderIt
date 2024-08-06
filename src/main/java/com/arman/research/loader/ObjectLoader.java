package com.arman.research.loader;

import com.arman.research.geom.polygons.PolygonGroup;
import com.arman.research.geom.polygons.TexturedPolygon3f;
import com.arman.research.geom.vectors.Vector3f;
import com.arman.research.render.lights.PointLight3f;
import com.arman.research.render.textures.Texture;
import com.arman.research.render.textures.ShadedSurface;
import com.arman.research.render.textures.ShadedTexture;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ObjectLoader {

    public static class Material {

        public File file;
        public ShadedTexture texture;

        public Material() {

        }

        public Material(File file, ShadedTexture texture) {
            this.file = file;
            this.texture = texture;
        }

    }

    public interface LineParser {

        void parseLine(String line) throws IOException, NumberFormatException, NoSuchElementException;

    }

    private File path;
    private List<Vector3f> vertices;
    private Material currentMaterial;
    private Map<String, Material> materials;
    private List<PointLight3f> lights;
    private float ambientLightIntensity;
    private Map<String, LineParser> parsers;
    private PolygonGroup object;
    private PolygonGroup currentGroup;

    public ObjectLoader() {
        materials = new HashMap<>();
        vertices = new ArrayList<>();
        parsers = new HashMap<>();
        parsers.put("obj", new ObjLineParser());
        parsers.put("mtl", new MtlLineParser());
        currentMaterial = null;
        setLights(new ArrayList<PointLight3f>(), 1);
    }

    public void setLights(List<PointLight3f> lights, float ambientLightIntensity) {
        this.lights = lights;
        this.ambientLightIntensity = ambientLightIntensity;
    }

    public PolygonGroup loadObject(String fileName) throws IOException {
        File file = new File(getClass().getClassLoader().getResource(fileName).toExternalForm().substring(6));
        object = new PolygonGroup();
        object.setFileName(file.getName());
        path = file.getParentFile();
        vertices.clear();
        currentGroup = object;
        parseFile(fileName);
        return object;
    }

    public Vector3f getVector(int i) {
        if (i < 0) {
            i = vertices.size() + i + 1;
        }
        return vertices.get(i - 1);
    }

    public void parseFile(String fileName) throws IOException {
        File file = new File(getClass().getClassLoader().getResource(fileName).getPath());
        BufferedReader br = new BufferedReader(new FileReader(file));
        LineParser parser = null;
        int ei = fileName.lastIndexOf('.');
        if (ei != -1) {
            String e = fileName.substring(ei + 1);
            parser = parsers.get(e.toLowerCase());
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
                try {
                    parser.parseLine(line);
                } catch (NumberFormatException | NoSuchElementException e) {
                    throw new IOException(e.getMessage());
                }
            }
        }
    }

    public class ObjLineParser implements LineParser {

        @Override
        public void parseLine(String line) throws IOException, NumberFormatException, NoSuchElementException {
            StringTokenizer tokenizer = new StringTokenizer(line);
            String tk = tokenizer.nextToken();
            if (tk.equals("v")) {
                vertices.add(new Vector3f(Float.parseFloat(tokenizer.nextToken()), Float.parseFloat(tokenizer.nextToken()), Float.parseFloat(tokenizer.nextToken())));
            } else if (tk.equals("f")) {
                List<Vector3f> curVertices = new ArrayList<>();
                while (tokenizer.hasMoreTokens()) {
                    String str = tokenizer.nextToken();
                    int i = str.indexOf('/');
                    if (i != -1) {
                        str = str.substring(0, i);
                    }
                    curVertices.add(getVector(Integer.parseInt(str)));
                }
                Vector3f[] arr = new Vector3f[curVertices.size()];
                curVertices.toArray(arr);
                TexturedPolygon3f p = new TexturedPolygon3f(arr);
                if (currentMaterial != null) {
                    ShadedSurface.createShadedSurface(p, currentMaterial.texture, lights, ambientLightIntensity);
                }
                currentGroup.add(p);
            } else if (tk.equals("getBottomY")) {
                if (tokenizer.hasMoreTokens()) {
                    String name = tokenizer.nextToken();
                    currentGroup = new PolygonGroup(name);
                } else {
                    currentGroup = new PolygonGroup();
                }
                object.add(currentGroup);
            } else if (tk.equals("mtllib")) {
                String name = tokenizer.nextToken();
                parseFile(name);
            } else if (tk.equals("usemtl")) {
                String name = tokenizer.nextToken();
                currentMaterial = materials.get(name);
            } else {

            }
        }

    }

    public class MtlLineParser implements LineParser {

        @Override
        public void parseLine(String line) throws IOException, NumberFormatException, NoSuchElementException {
            StringTokenizer tokenizer = new StringTokenizer(line);
            String tk = tokenizer.nextToken();
            if (tk.equals("newmtl")) {
                String name = tokenizer.nextToken();
                currentMaterial = materials.get(name);
                if (currentMaterial == null) {
                    currentMaterial = new Material();
                    materials.put(name, currentMaterial);
                }
            } else if (tk.equals("map_Kd")) {
                String name = tokenizer.nextToken();
                File file = new File(path, name);
                if (!file.equals(currentMaterial.file)) {
                    currentMaterial.file = file;
                    currentMaterial.texture = (ShadedTexture) Texture.createTexture(file.getPath(), true);
                }
            } else {

            }
        }

    }

}
