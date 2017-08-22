package io.piotrjastrzebski.bte.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.branch.*;
import com.badlogic.gdx.ai.btree.decorator.AlwaysFail;
import com.badlogic.gdx.ai.btree.decorator.AlwaysSucceed;
import com.badlogic.gdx.ai.btree.decorator.Include;
import com.badlogic.gdx.ai.btree.leaf.Failure;
import com.badlogic.gdx.ai.btree.leaf.Success;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeLibrary;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeLibraryManager;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeParser;
import com.badlogic.gdx.ai.utils.random.TriangularIntegerDistribution;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisTextButton;
import io.piotrjastrzebski.bte.AIEditor;
import io.piotrjastrzebski.bte.EditorBehaviourTreeLibrary;
import io.piotrjastrzebski.bte.desktop.dog.*;

/**
 * Created by EvilEntity on 04/02/2016.
 */
public class AIEditorTestProject extends Game {
	private static final String TAG = AIEditorTestProject.class.getSimpleName();

	private SpriteBatch batch;
	private Stage stage;
	private Table root;
	private Skin skin;
	private AIEditor editor;
	private BehaviorTree<Dog> tree;
	private VisTextButton toggle;

	@Override public void create () {
		BehaviorTreeLibraryManager libraryManager = BehaviorTreeLibraryManager.getInstance();
		// EditorBehaviourTreeLibrary should be used for some extra things
		BehaviorTreeLibrary library = new EditorBehaviourTreeLibrary(BehaviorTreeParser.DEBUG_HIGH);
		registerDogBehavior(library);
		libraryManager.setLibrary(library);
		tree = libraryManager.createBehaviorTree("dog", new Dog("Buddy"));

		batch = new SpriteBatch();
		stage = new Stage(new ScreenViewport(), batch);
		Gdx.input.setInputProcessor(stage);
		root = new Table();
		root.setFillParent(true);
		stage.addActor(root);
		// TODO need better detection maybe
		if (Gdx.graphics.getPpiX() > 100) {
			VisUI.load(VisUI.SkinScale.X2);
		} else {
			VisUI.load(VisUI.SkinScale.X1);
		}
		skin = VisUI.getSkin();

		editor = new AIEditor(skin);
		// we probably don't care about the blackboard, do we?
		editor.initialize(tree);
		// add default task classes to task drawer with default tags
		editor.addDefaultTaskClasses();
		editor.setUpdateStrategy(new AIEditor.BehaviorTreeStepStrategy() {
			float timer;
			@Override public boolean shouldStep (BehaviorTree tree, float delta) {
				timer += delta;
				if (timer >= 1) {
					timer -= 1;
					return true;
				}
				return false;
			}
		});
		addTaskClasses();
		toggle = new VisTextButton("Show editor");
		toggle.addListener(new ClickListener(){
			@Override public void clicked (InputEvent event, float x, float y) {
				toggleEditorWindow();
			}
		});
		toggleEditorWindow();
		root.add(toggle).expand().left().bottom().pad(10);

		VisDialog tutorial = new VisDialog("Tutorial");
		tutorial.addCloseButton();
		tutorial.text(
			"top - menu, undo/redo buttons\n"
			+ "left - tasks that can be added to the tree\n"
			+ "centre - behaviour tree you are editing\n"
			+ "right - selected task properties - nyi\n"
			+ "\n"
			+ "drag and drop:\n"
			+ " - drawer -> tree: add task to tree\n"
			+ " - tree -> drawer: remove task from tree\n"
			+ " - tree -> tree: move tasks, hold ctrl to copy instead\n"
			+ "\n"
			+ "click task in tree to edit its parameters - nyi\n"
			+ "TODO b lot of crap\n");
		tutorial.pack();
//		tutorial.show(stage);
	}

	private void addTaskClasses () {
		editor.addTaskClass("dog", BarkTask.class);
		editor.addTaskClass("dog", CareTask.class, false);
		editor.addTaskClass("dog", MarkTask.class);
		editor.addTaskClass("dog", RestTask.class, false);
		editor.addTaskClass("dog", WalkTask.class);
	}

	private void toggleEditorWindow () {
		editor.toggleWindow(root);
		// TODO maybe an event
		if (editor.isWindowVisible()) {
			toggle.setText("Hide editor");
		} else {
			toggle.setText("Show editor");
		}
	}

	private void registerDogBehavior (BehaviorTreeLibrary library) {

		Include<Dog> include = new Include<>();
		include.lazy = false;
		include.subtree = "dog.actual";
		BehaviorTree<Dog> includeBehavior = new BehaviorTree<>(include);
		library.registerArchetypeTree("dog", includeBehavior);

		BehaviorTree<Dog> actualBehavior = new BehaviorTree<>(createDogBehavior());
		library.registerArchetypeTree("dog.actual", actualBehavior);

		BehaviorTree<Dog> other = new BehaviorTree<>(createDogOtherBehavior());
		library.registerArchetypeTree("dog.other", other);
	}

	private static Task<Dog> createDogBehavior () {
		// TODO nested guards explode :/

		RandomSelector<Dog> guard2A = new RandomSelector<>();
		guard2A.addChild(new Failure<Dog>());
		guard2A.addChild(new Success<Dog>());
		Failure<Dog> guardedFailure2 = new Failure<>();
		guardedFailure2.setGuard(guard2A);

		RandomSelector<Dog> guard = new RandomSelector<>();
		RandomSelector<Dog> guard2 = new RandomSelector<>();
		guard2.addChild(guardedFailure2);
		guard2.addChild(new Success<Dog>());

		Failure<Dog> guardedFailure = new Failure<>();
		guardedFailure.setGuard(guard2);
		guard.addChild(guardedFailure);
		guard.addChild(new Success<Dog>());


		Selector<Dog> selector = new Selector<>();

		DynamicGuardSelector<Dog> guardSelector = new DynamicGuardSelector<>();
		AlwaysFail<Dog> walkFail = new AlwaysFail<>();
		walkFail.setGuard(guard);
		WalkTask guardedWalk = new WalkTask();
		walkFail.addChild(guardedWalk);
		guardSelector.addChild(walkFail);
		AlwaysFail<Dog> alwaysFail = new AlwaysFail<>();
		alwaysFail.addChild(new CareTask());
		guardSelector.addChild(alwaysFail);

		selector.addChild(guardSelector);

		Parallel<Dog> parallel = new Parallel<>();
		selector.addChild(parallel);

		CareTask care = new CareTask();
		care.urgentProb = 0.8f;
		parallel.addChild(care);
		parallel.addChild(new AlwaysFail<>(new RestTask()));

		Sequence<Dog> sequence = new Sequence<>();
		selector.addChild(sequence);


		RandomSelector<Dog> guard3 = new RandomSelector<>();
		guard3.addChild(new Failure<Dog>());
		guard3.addChild(new Success<Dog>());

		BarkTask bark1 = new BarkTask();
		bark1.times = new TriangularIntegerDistribution(1, 5, 2);
		bark1.setGuard(guard3);
		sequence.addChild(bark1);
		sequence.addChild(new WalkTask());
		sequence.addChild(new BarkTask());
		sequence.addChild(new MarkTask());

		return selector;
	}

	private static Task<Dog> createDogOtherBehavior () {
		Parallel<Dog> parallel = new Parallel<>();
		parallel.addChild(new AlwaysSucceed<>(new RestTask()));
		parallel.addChild(new BarkTask());
		return parallel;
	}

	@Override public void render () {
		Gdx.gl.glClearColor(.5f, .5f, .5f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		editor.update(Gdx.graphics.getDeltaTime());
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override public void resize (int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override public void dispose () {
		editor.dispose();
		batch.dispose();
		VisUI.dispose();
	}

	public static void main (String[] args) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(800, 600);
		config.setHdpiMode(Lwjgl3ApplicationConfiguration.HdpiMode.Logical);
		new Lwjgl3Application(new AIEditorTestProject(), config);
	}
}
