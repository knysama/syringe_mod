// Made with Blockbench 5.1.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


public class syringe_bag_model<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "syringe_bag_model"), "main");
	private final ModelPart syringe_bag_root;
	private final ModelPart body;
	private final ModelPart shell;
	private final ModelPart trim;
	private final ModelPart cavity;
	private final ModelPart hardware;
	private final ModelPart latch;
	private final ModelPart indicator;
	private final ModelPart left_loop;
	private final ModelPart right_loop;
	private final ModelPart hinge_body;
	private final ModelPart ampoules;
	private final ModelPart ampoule_red;
	private final ModelPart ampoule_orange;
	private final ModelPart ampoule_yellow;
	private final ModelPart ampoule_green;
	private final ModelPart ampoule_blue;
	private final ModelPart lid;
	private final ModelPart lid_shell;
	private final ModelPart lid_inner;
	private final ModelPart hinge_lid;

	public syringe_bag_model(ModelPart root) {
		this.syringe_bag_root = root.getChild("syringe_bag_root");
		this.body = this.syringe_bag_root.getChild("body");
		this.shell = this.body.getChild("shell");
		this.trim = this.body.getChild("trim");
		this.cavity = this.body.getChild("cavity");
		this.hardware = this.syringe_bag_root.getChild("hardware");
		this.latch = this.hardware.getChild("latch");
		this.indicator = this.hardware.getChild("indicator");
		this.left_loop = this.hardware.getChild("left_loop");
		this.right_loop = this.hardware.getChild("right_loop");
		this.hinge_body = this.hardware.getChild("hinge_body");
		this.ampoules = this.syringe_bag_root.getChild("ampoules");
		this.ampoule_red = this.ampoules.getChild("ampoule_red");
		this.ampoule_orange = this.ampoules.getChild("ampoule_orange");
		this.ampoule_yellow = this.ampoules.getChild("ampoule_yellow");
		this.ampoule_green = this.ampoules.getChild("ampoule_green");
		this.ampoule_blue = this.ampoules.getChild("ampoule_blue");
		this.lid = this.syringe_bag_root.getChild("lid");
		this.lid_shell = this.lid.getChild("lid_shell");
		this.lid_inner = this.lid.getChild("lid_inner");
		this.hinge_lid = this.lid.getChild("hinge_lid");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition syringe_bag_root = partdefinition.addOrReplaceChild("syringe_bag_root", CubeListBuilder.create(), PartPose.offset(-8.0F, 16.0F, 8.0F));

		PartDefinition body = syringe_bag_root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition shell = body.addOrReplaceChild("shell", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -2.75F, 1.0F, 12.0F, 6.75F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.0F, -2.75F, -2.0F, 12.0F, 6.75F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(5.0F, -2.75F, -1.0F, 1.0F, 6.75F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.0F, -2.75F, -1.0F, 1.0F, 6.75F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition trim = body.addOrReplaceChild("trim", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, 3.25F, 1.9F, 12.0F, 0.75F, 0.3F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.0F, -2.75F, 1.8F, 12.0F, 0.75F, 0.4F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(5.5F, -2.0F, 1.9F, 0.5F, 5.25F, 0.25F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.0F, -2.0F, 1.9F, 0.5F, 5.25F, 0.25F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cavity = body.addOrReplaceChild("cavity", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, 3.0F, -1.0F, 10.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition hardware = syringe_bag_root.addOrReplaceChild("hardware", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition latch = hardware.addOrReplaceChild("latch", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.25F, 0.0F, 3.0F, 3.0F, 0.25F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-1.0F, -1.5F, 0.25F, 2.0F, 3.0F, 0.75F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.5F, -0.75F, 1.0F, 1.0F, 1.5F, 0.25F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.75F, 1.25F, 1.0F, 1.5F, 0.5F, 0.25F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 2.0F));

		PartDefinition indicator = hardware.addOrReplaceChild("indicator", CubeListBuilder.create().texOffs(0, 0).addBox(-1.25F, -0.75F, 0.0F, 2.5F, 1.5F, 0.25F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.75F, -0.35F, 0.25F, 1.5F, 0.7F, 0.15F, new CubeDeformation(0.0F)), PartPose.offset(-3.75F, 2.25F, 2.0F));

		PartDefinition left_loop = hardware.addOrReplaceChild("left_loop", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -1.75F, -1.0F, 1.5F, 0.75F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(0.0F, 1.0F, -1.0F, 1.5F, 0.75F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(1.25F, -1.0F, -1.0F, 0.75F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, 0.5F, 0.0F));

		PartDefinition right_loop = hardware.addOrReplaceChild("right_loop", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.75F, -1.0F, 1.5F, 0.75F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-1.5F, 1.0F, -1.0F, 1.5F, 0.75F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-2.0F, -1.0F, -1.0F, 0.75F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, 0.5F, 0.0F));

		PartDefinition hinge_body = hardware.addOrReplaceChild("hinge_body", CubeListBuilder.create().texOffs(0, 0).addBox(3.0F, -0.25F, -0.25F, 2.0F, 0.75F, 0.75F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-5.0F, -0.25F, -0.25F, 2.0F, 0.75F, 0.75F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.75F, -2.0F));

		PartDefinition ampoules = syringe_bag_root.addOrReplaceChild("ampoules", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition ampoule_red = ampoules.addOrReplaceChild("ampoule_red", CubeListBuilder.create().texOffs(0, 0).addBox(-0.9F, -0.5F, -0.8F, 1.8F, 0.25F, 1.6F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.75F, -2.25F, -0.65F, 1.5F, 1.75F, 1.3F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.75F, -3.0F, -0.65F, 1.5F, 0.75F, 1.3F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.0F, 0.0F));

		PartDefinition ampoule_orange = ampoules.addOrReplaceChild("ampoule_orange", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -0.5F, -0.8F, 1.8F, 0.25F, 1.6F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.75F, -2.25F, -0.65F, 1.5F, 1.75F, 1.3F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.75F, -3.0F, -0.65F, 1.5F, 0.75F, 1.3F, new CubeDeformation(0.0F)), PartPose.offset(2.2F, 0.0F, 0.0F));

		PartDefinition ampoule_yellow = ampoules.addOrReplaceChild("ampoule_yellow", CubeListBuilder.create().texOffs(0, 0).addBox(-1.1F, -0.5F, -0.8F, 1.8F, 0.25F, 1.6F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.75F, -2.25F, -0.65F, 1.5F, 1.75F, 1.3F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.75F, -3.0F, -0.65F, 1.5F, 0.75F, 1.3F, new CubeDeformation(0.0F)), PartPose.offset(0.4F, 0.0F, 0.0F));

		PartDefinition ampoule_green = ampoules.addOrReplaceChild("ampoule_green", CubeListBuilder.create().texOffs(0, 0).addBox(-1.2F, -0.5F, -0.8F, 1.8F, 0.25F, 1.6F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.75F, -2.25F, -0.65F, 1.5F, 1.75F, 1.3F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.75F, -3.0F, -0.65F, 1.5F, 0.75F, 1.3F, new CubeDeformation(0.0F)), PartPose.offset(-1.4F, 0.0F, 0.0F));

		PartDefinition ampoule_blue = ampoules.addOrReplaceChild("ampoule_blue", CubeListBuilder.create().texOffs(0, 0).addBox(-1.3F, -0.5F, -0.8F, 1.8F, 0.25F, 1.6F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.75F, -2.25F, -0.65F, 1.5F, 1.75F, 1.3F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.75F, -3.0F, -0.65F, 1.5F, 0.75F, 1.3F, new CubeDeformation(0.0F)), PartPose.offset(-3.2F, 0.0F, 0.0F));

		PartDefinition lid = syringe_bag_root.addOrReplaceChild("lid", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -2.75F, -2.0F, 1.2217F, 0.0F, 0.0F));

		PartDefinition lid_shell = lid.addOrReplaceChild("lid_shell", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -0.75F, 0.0F, 12.0F, 0.75F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lid_inner = lid.addOrReplaceChild("lid_inner", CubeListBuilder.create().texOffs(0, 0).addBox(-5.5F, 0.0F, 0.5F, 11.0F, 0.15F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition hinge_lid = lid.addOrReplaceChild("hinge_lid", CubeListBuilder.create().texOffs(0, 0).addBox(3.25F, -0.6F, -0.25F, 1.5F, 0.65F, 0.75F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-4.75F, -0.6F, -0.25F, 1.5F, 0.65F, 0.75F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		syringe_bag_root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}