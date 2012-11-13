package com.valdemar.tellatale.services;

import java.util.Arrays;

import com.stackmob.sdk.api.StackMob;
import com.stackmob.sdk.callback.StackMobCallback;
import com.stackmob.sdk.exception.StackMobException;
import com.valdemar.tellatale.model.Dummy;
import com.valdemar.tellatale.model.Tail;
import com.valdemar.tellatale.model.Tale;
import com.valdemar.tellatale.services.AddTailService.OnAddTailListner;

public class StartTaleService {

	public interface OnStartTaleListner {
		public void onSuccess();

		public void onFailure();

		public void onFailureOnAddTail();
	}

	private OnStartTaleListner onStartTaleListner;
	private Tale tale;
	private Tail tail;

	public StartTaleService(Tale tale, Tail tail, OnStartTaleListner onStartTaleListner) {
		this.tail = tail;
		this.tale = tale;
		setOnStartTaleListner(onStartTaleListner);

	}

	public void setOnStartTaleListner(OnStartTaleListner onStartTaleListner) {
		this.onStartTaleListner = onStartTaleListner;
	}

	public void doStartATale() {
		
		
		Dummy dummy;
		dummy = new Dummy("Teste 1 - Valdemar");
//		dummy.save();

		
		
		
		//TODO: Comentei...
//		StackMob stackmob = StackMobCommon.getStackMobInstance();
//
//		stackmob.postRelatedBulk("user", "jonh", "dummies", Arrays.asList(dummy), addTaleStackMobCallback);
	}

	StackMobCallback addTaleStackMobCallback = new StackMobCallback() {

		@Override
		public void success(String arg0) {

			onStartTaleListner.onSuccess();

			// addTheTail(tail, "XXX");
		}

		@Override
		public void failure(StackMobException arg0) {
			onStartTaleListner.onFailure();

		}
	};

	private void addTheTail(Tail tail, String taleID) {
		AddTailService addTaile = new AddTailService(onAddTailListner);
		addTaile.doAddTail(tail, taleID);
	}

	OnAddTailListner onAddTailListner = new OnAddTailListner() {

		@Override
		public void onSuccess() {
			onStartTaleListner.onSuccess();

		}

		@Override
		public void onFailure() {
			onStartTaleListner.onFailureOnAddTail();

		}
	};
}
