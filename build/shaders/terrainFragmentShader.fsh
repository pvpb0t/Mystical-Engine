#version 400 core

in vec2 pass_textureCoordinates;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float visibility;

out vec4 out_Color;

//uniform variables can be accessed and modified by the shader class
uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap;


uniform vec3 lightColour;
uniform float shineFactor;
uniform float reflectionFactor;
uniform vec3 skyColour;

void main(void){

    vec4 blendMapColour = texture(blendMap, pass_textureCoordinates);

    float backTextureAmount = 1-(blendMapColour.r+blendMapColour.g+blendMapColour.b);
    vec2 tiledCoords = pass_textureCoordinates*40.0;
    vec4 backgroundTextureColour = texture(backgroundTexture, tiledCoords)*backTextureAmount;
    vec4 rTextureColour = texture(rTexture,tiledCoords) * blendMapColour.r;
    vec4 gTextureColour = texture(gTexture,tiledCoords) * blendMapColour.g;
    vec4 bTextureColour = texture(bTexture,tiledCoords) * blendMapColour.b;

    vec4 totalColour = backgroundTextureColour +rTextureColour+gTextureColour+bTextureColour;

	vec3 unitNormal  = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);

	float nDot1 = dot(unitNormal, unitLightVector);
	float brightness = max(nDot1, 0.2);  // the only reason to do this step is because sometimes the dot product returns values less than 0 (if vectors are facing away from each other). Since we don't care about values less than 0, we take this step.

    vec3 unitVectorToCamera = normalize(toCameraVector);
    vec3 lightDirection = -unitLightVector;
    vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);

    //https://youtu.be/GZ_1xOm-3qU?t=632 2022-09-16
    float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
    specularFactor = max(specularFactor, 0.0);
    float dampedFactor = pow(specularFactor, shineFactor);
    vec3 finalSpecular = dampedFactor * reflectionFactor *lightColour;

	vec3 diffuse = brightness*lightColour;
	out_Color = vec4(diffuse, 1.0) * totalColour + vec4(finalSpecular, 1.0);
	out_Color = mix(vec4(skyColour,1.0),out_Color, visibility);
}