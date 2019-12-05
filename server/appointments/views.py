from django.shortcuts import get_object_or_404
from django.http import HttpResponse, JsonResponse
from rest_framework import status
from rest_framework.response import Response
from rest_framework.decorators import api_view
from credentials.serializers import UserSerializer
from django.contrib.auth.models import User
from .models import Appointment
from .serializers import AppointmentSerializer
from django.db.models import Q
from itertools import zip_longest


@api_view(['GET'])
def available_appointments(request, username):
    """Return appointments by username"""
    user = get_object_or_404(User, username=username)
    appointments = Appointment.objects.available().filter(tutor = user).order_by('time')
    data = [AppointmentSerializer(x).data for x in appointments]
    return Response(data = {"results": [ {"id": x["id"], "time": x["time"],
                                          "appointment_string": str(y)}
                                         for (x,y) in zip_longest(data, appointments)]})


@api_view(['GET'])
def user_appointment_list(request, username):
    """Appointments where user is either tutor or appointee"""
    user = get_object_or_404(User, username=username)
    where_tutor = Q(tutor = user)
    where_appointee = Q(appointee = username)
    appointments = Appointment.objects.filter(
        where_tutor | where_appointee)
    data = [AppointmentSerializer(x).data for x in appointments]
    for (obj,app) in zip_longest(data,appointments):
        obj['tutor'] = User.objects.get(pk = obj['tutor']).username
        obj['appointment_string'] = str(app)
    return Response(data = {'results': data})

@api_view(['POST'])
def set_availability(request):
    """Tutor sets available appointments"""

    data = {'username': request.data['username'], 'time': request.data['time']}
    print(data)
    message = {"status": "fail"}
    try:
        tutor = get_object_or_404(User, username = data['username'])
    except Exception as e:
        print('exception',e)
        return Response(data = message)

    data['tutor'] = tutor.pk
    appointment = AppointmentSerializer(data = data)
    if appointment.is_valid():
        a = appointment.save()
        a.save()
        message['status'] = "success"
        return Response(data = message)
    else:
        return Response(data = appointment.errors)


@api_view(['POST'])
def book_appointment(request):
    """Mark appointment taken by user"""
    message = {'status': 'fail'}
    data = {'id': request.data['id'], 'appointee': request.data['username']}
    appointee = get_object_or_404(User, username = data['appointee'])
    appointment = Appointment.objects.available().filter(id = data['id'])
    if appointment.count() == 1:
        appointment = appointment[0]
        if appointment.tutor == appointee:
            return Response(data = message)
        appointment.appointee = appointee.username
        appointment.save()
        message['status'] = 'success'
    return Response(data = message)
